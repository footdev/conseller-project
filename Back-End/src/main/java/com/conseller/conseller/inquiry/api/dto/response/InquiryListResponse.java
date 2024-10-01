package com.conseller.conseller.inquiry.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InquiryListResponse {
    private List<InquiryItemData> items;
}
