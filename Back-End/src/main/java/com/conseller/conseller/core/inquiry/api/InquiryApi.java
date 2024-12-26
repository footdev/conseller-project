package com.conseller.conseller.core.inquiry.api;

import com.conseller.conseller.core.inquiry.api.dto.request.AnswerInquiryRequest;
import com.conseller.conseller.core.inquiry.api.dto.request.RegistInquiryRequest;
import com.conseller.conseller.core.inquiry.api.dto.response.DetailInquiryResponse;
import com.conseller.conseller.core.inquiry.api.dto.response.InquiryItemResponse;
import com.conseller.conseller.core.inquiry.api.dto.response.InquiryListResponse;
import com.conseller.conseller.core.inquiry.domain.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiryApi {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<Object> registInquiry(@RequestBody RegistInquiryRequest request) {
        inquiryService.registInquiry(request.getUserIdx(), request.getReportedUserIdx(), request.toAddInquiry());
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping
    public ResponseEntity<InquiryListResponse> getInquiryList() {
        return ResponseEntity.ok()
                .body(new InquiryListResponse(InquiryItemResponse.ofAll(inquiryService.getInquires())));
    }

    @GetMapping("/{inquiryIdx}")
    public ResponseEntity<DetailInquiryResponse> detailInquiry(@PathVariable("inquiryIdx") long inquiryIdx) {
        return ResponseEntity.ok()
                .body(inquiryService.detailInquiry(inquiryIdx));
    }


    @PatchMapping("/{inquiryIdx}")
    public ResponseEntity<Object> answerInquiry(@PathVariable("inquiryIdx") long inquiryIdx, @RequestBody AnswerInquiryRequest request) {
        inquiryService.answerInquiry(inquiryIdx, request);
        return ResponseEntity.ok()
                .build();
    }
}
