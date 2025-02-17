package com.conseller.conseller.core.user.api.dto.request;

import lombok.*;

@Builder
@Getter
public class EmailAndIdRequest {
    private String userEmail;
    private String userId;
}
