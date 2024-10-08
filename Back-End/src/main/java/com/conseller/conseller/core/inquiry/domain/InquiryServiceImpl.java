package com.conseller.conseller.core.inquiry.domain;

import com.conseller.conseller.core.inquiry.infrastructure.InquiryEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.inquiry.infrastructure.InquiryRepository;
import com.conseller.conseller.core.inquiry.api.dto.mapper.InquiryMapper;
import com.conseller.conseller.core.inquiry.api.dto.request.AnswerInquiryRequest;
import com.conseller.conseller.core.inquiry.api.dto.request.RegistInquiryRequest;
import com.conseller.conseller.core.inquiry.api.dto.response.DetailInquiryResponse;
import com.conseller.conseller.core.inquiry.api.dto.response.InquiryItemData;
import com.conseller.conseller.core.inquiry.api.dto.response.InquiryListResponse;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryServiceImpl implements InquiryService{
    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;

    @Override
    public void registInquiry(RegistInquiryRequest request) {
        UserEntity userEntity = userRepository.findById(request.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));
        UserEntity reportedUserEntity = userRepository.findById(request.getReportedUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        InquiryEntity inquiryEntity = InquiryMapper.INSTANCE.registInquiryToInquiry(request, userEntity, reportedUserEntity);

        inquiryRepository.save(inquiryEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public InquiryListResponse getInquiryList() {
        List<InquiryEntity> inquiryEntityList = inquiryRepository.findAll(Sort.by(Sort.Order.desc("inquiryCreatedDate")));

        List<InquiryItemData> itemData = InquiryMapper.INSTANCE.inquirysToItemDatas(inquiryEntityList);

        InquiryListResponse response = new InquiryListResponse(itemData);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public DetailInquiryResponse detailInquiry(Long inquiryIdx) {
        InquiryEntity inquiryEntity = inquiryRepository.findById(inquiryIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INQUIRY_INVALID));

        UserEntity userEntity = userRepository.findById(inquiryEntity.getUserEntity().getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        DetailInquiryResponse response = InquiryMapper.INSTANCE.entityToDetailInquiryResponse(userEntity, inquiryEntity);

        return response;
    }

    @Override
    public void answerInquiry(Long inquiryIdx, AnswerInquiryRequest request) {
        InquiryEntity inquiryEntity = inquiryRepository.findById(inquiryIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INQUIRY_INVALID));

        inquiryEntity.setInquiryAnswer(request.getInquiryAnswer());
        inquiryEntity.setInquiryAnswerDate(LocalDateTime.now());

    }
}
