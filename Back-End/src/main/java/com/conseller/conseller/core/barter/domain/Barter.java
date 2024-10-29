package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.category.infrastructure.SubCategory;
import com.conseller.conseller.core.user.infrastructure.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Barter {
    private long barterIdx;
    private String barterName;
    private String barterText;
    private LocalDateTime barterCreatedDate;
    private LocalDateTime barterEndDate;
    private LocalDateTime barterModifiedDate;
    private LocalDateTime barterCompletedDate;
    private String barterStatus;
    private User barterHost;
    private User barterCompleteGuest;
    private SubCategory subCategory;
    private SubCategory preferSubCategory;
}
