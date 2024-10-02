package com.conseller.conseller.user.api.dto;

import com.conseller.conseller.user.infrastructure.UserEntity;
import com.conseller.conseller.user.api.dto.request.SignUpRequest;
import com.conseller.conseller.user.api.dto.response.UserInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "userPassword", target = "userPassword")
    @Mapping(source = "userNickname", target = "userNickname")
    @Mapping(source = "userEmail", target = "userEmail")
    @Mapping(source = "userProfileUrl", target = "userProfileUrl")
    @Mapping(source = "userAccount", target = "userAccount")
    @Mapping(source = "userAccountBank", target = "userAccountBank")
    @Mapping(source = "userPhoneNumber", target = "userPhoneNumber")
    UserInfoResponse toUserInfoResponse(UserEntity userEntity);

    @Mapping(target = "userAccountBank",
            expression = "java(com.conseller.conseller.user.enums.AccountBanks.fromString(signUpRequest.getUserAccountBank()).getBank())")
    UserEntity signUpDtoToUser(SignUpRequest signUpRequest);
}
