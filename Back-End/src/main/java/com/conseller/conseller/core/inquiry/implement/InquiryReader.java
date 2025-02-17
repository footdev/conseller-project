package com.conseller.conseller.core.inquiry.implement;

import com.conseller.conseller.core.inquiry.domain.Inquiry;
import com.conseller.conseller.core.inquiry.infrastructure.InquiryEntity;
import com.conseller.conseller.core.inquiry.infrastructure.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InquiryReader {

    private final InquiryRepository inquiryRepository;

    public List<Inquiry> readAll() {
        return inquiryRepository.findAll(Sort.by(Sort.Order.desc("inquiryCreatedDate")))
                .stream()
                .map(InquiryEntity::toDomain)
                .collect(Collectors.toList());

    }

    public Inquiry read(long inquiryIdx) {
        return inquiryRepository.findById(inquiryIdx)
                .orElseThrow(() -> new RuntimeException("Invalid Inquiry"))
                .toDomain();
    }
}
