package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.BarterCreateRequest;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.category.domain.SubCategory;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

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
    private User host;
    private User barterCompleteGuest;
    private SubCategory subCategory;
    private SubCategory preferSubCategory;

    public String getPreferSubCategoryContent() {
        return preferSubCategory.getSubCategoryContent();
    }

    public Long getHostIdx() {
        return host.getUserIdx();
    }

    public String getHostProfileUrl() {
        return host.getUserProfileUrl();
    }

    public Long getHostDeposit() {
        return host.getUserDeposit();
    }

    public String getHostNickname() {
        return host.getUserNickname();
    }

    public static Barter of(BarterCreateRequest barterCreateRequest, User host, SubCategory preferSubCategory, SubCategory maxSubCategory, LocalDateTime createdDate) {
        return Barter.builder()
                .barterName(barterCreateRequest.getBarterName())
                .barterText(barterCreateRequest.getBarterText())
                .barterCreatedDate(createdDate)
                .barterEndDate(LocalDateTime.parse(barterCreateRequest.getBarterEndDate()))
                .barterStatus(BarterStatus.EXCHANGEABLE.getStatus())
                .host(host)
                .subCategory(maxSubCategory)
                .preferSubCategory(preferSubCategory)
                .build();
    }

    public void modify(SubCategory preferSubCategory, String barterName, String barterText, LocalDateTime barterEndDate) {
        this.preferSubCategory = preferSubCategory;
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterEndDate = barterEndDate;
    }
}
