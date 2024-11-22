package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.*;
import com.conseller.conseller.core.barter.api.dto.response.*;
import com.conseller.conseller.core.barter.implement.*;
import com.conseller.conseller.core.category.domain.SubCategory;
import com.conseller.conseller.core.category.implement.SubCategoryReader;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.implement.GifticonModifier;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.core.gifticon.implement.GifticonValidator;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserReader;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarterService {

    private final BarterReader barterReader;
    private final BarterAppender barterAppender;
    private final BarterModifier barterModifier;
    private final BarterRemover barterRemover;
    private final BarterProcessor barterProcessor;

    private final BarterHostItemReader barterHostItemReader;
    private final BarterHostItemAppender barterHostItemAppender;
    private final BarterHostItemValidator barterHostItemValidator;
    private final BarterHostItemRemover barterHostItemRemover;

    private final BarterRequestReader barterRequestReader;
    private final BarterRequestRemover barterRequestRemover;
    private final BarterRequestManager barterRequestManager;

    private final BarterGuestItemReader barterGuestItemReader;

    private final UserReader userReader;

    private final SubCategoryReader subCategoryReader;

    private final GifticonReader gifticonReader;
    private final GifticonModifier gifticonModifier;
    private final GifticonValidator gifticonValidator;

    public BarterPagingResponse getBarters(BarterFilterRequest barterFilterRequest) {
        Pageable pageable = PageRequest.of(barterFilterRequest.getPage() - 1, 10);
        Page<Barter> barters = barterReader.readBartersByPaging(barterFilterRequest, pageable);
        return BarterPagingResponse.of(
                barters.getTotalPages(),
                barters.getTotalElements(),
                barters.stream()
                        .map(barter -> barterHostItemReader.readByBarterId(barter.getBarterIdx()))
                        .map(BarterHostItemResponse::of)
                        .collect(Collectors.toList())
        );
    }

    public BarterDetailResponse getBarter(Long barterIdx, Long userIdx) {
        Barter barter = barterReader.read(barterIdx);
        List<BarterHostItem> barterHostItems = barterHostItemReader.readAll(barterIdx);
        return BarterDetailResponse.of(barter, BarterItemResponse.of(barterHostItems));
    }


    @Transactional
    public Long createBarter(BarterCreateRequest barterCreateRequest) {
        User host = userReader.read(barterCreateRequest.getUserIdx());
        SubCategory preferSubCategory = subCategoryReader.read(barterCreateRequest.getPreferSubCategory());
        SubCategory maxSubCategory = subCategoryReader.read(barterProcessor.getMaxSelectedCategory(barterCreateRequest.getHostItemIdxs()));
        List<Gifticon> hostItems = gifticonReader.readAll(barterCreateRequest.getHostItemIdxs());

        barterHostItemValidator.verifyUserGifticonMatch(hostItems, barterCreateRequest.getUserIdx());
        barterHostItemValidator.isKeep(hostItems);

        Barter barter = Barter.of(barterCreateRequest, host, preferSubCategory, maxSubCategory, now());
        Long savedBarterId = barterAppender.append(barter);

        barterHostItemAppender.appendAll(BarterHostGifticons.from(barterHostItemReader.convert(hostItems, barter)));

        return savedBarterId;
    }

    @Transactional
    public void modifyBarter(Long barterIdx, BarterModifyRequest barterModifyRequest) {
        SubCategory preferSubCategory = subCategoryReader.read(barterModifyRequest.getSubCategory());
        Barter barter = barterReader.read(barterIdx);
        barterModifier.modify(barter, preferSubCategory, barterModifyRequest);
    }

    @Transactional
    public void deleteBarter(Long barterId) {
        barterRemover.remove(barterId);
        barterRequestRemover.removeAll(barterId);
    }

    @Transactional
    public void exchangeGifticon(Long barterId, Long userId, Long barterRequestId) {
        User host = userReader.read(userId);
        Barter barter = barterReader.read(barterId);

        BarterRequest acceptedRequest = barterRequestReader.read(barterRequestId);
        List<BarterRequest> barterRequests = barterRequestReader.readAll(barterId);

        Gifticons hostGifticons = BarterHostGifticons.from(barterHostItemReader.readAll(barter.getBarterIdx()));
        Gifticons guestGifticons = BarterGuestGifticons.from(barterGuestItemReader.readAll(barterRequestId));

        gifticonValidator.isBarterAll(hostGifticons.getGifticons());
        gifticonValidator.isBarterAll(guestGifticons.getGifticons());

        barterRequestManager.acceptAt(acceptedRequest);
        barterRequestManager.rejectOtherRequests(barterRequests, acceptedRequest.getBarterRequestIdx());
        barterProcessor.exchangeGifticons(hostGifticons, guestGifticons, host, acceptedRequest.getUser());
        barterModifier.accept(barter, acceptedRequest.getUser());
    }

    public BarterRequestsResponse getBarterRequests(Long barterIdx) {
        Barter barter = barterReader.read(barterIdx);
        List<BarterRequest> barterRequests = barterRequestReader.readAll(barterIdx);
        List<BarterRequestResponse> barterRequestResponses = barterRequests.stream()
                .map(barterRequest -> BarterRequestResponse.of(barterRequest, barterRequest.getGifticons()))
                .collect(Collectors.toList());
        return BarterRequestsResponse.of(barterIdx, barterRequestResponses);
    }
}
