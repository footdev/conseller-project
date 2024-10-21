package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
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
    private UserEntity barterHost;
    private UserEntity barterCompleteGuest;
    private SubCategoryEntity subCategoryEntity;
    private SubCategoryEntity preferSubCategoryEntity;
}
