package com.conseller.conseller.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomExceptionStatus {
    //로그인 관련
    ALREADY_EXIST_NICKNAME(997, "이미 존재하는 닉네임입니다."),
    ALREADY_EXIST_EMAIL(998, "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_PHONE_NUMBER(999, "이미 존재하는 휴대번호입니다."),
    WRONG_ID(1000, "잘못된 아이디입니다."),
    WRONG_PW(1001, "잘못된 비밀번호입니다."),
    RESTRICT_USER(1002, "제한된 유저입니다."),
    INVALID_ACCESS_TOKEN(1003, "유효하지 않은 엑세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(1004, "유효하지 않은 리프레시 토큰입니다."),
    INVALID_PATTERN(1005, "잘못된 패턴입니다."),
    INVALID_FIND_ID(1006, "유효하지 않은 정보입니다."),
    INVALID_FIND_PW(1007, "유효하지 않은 정보입니다."),
    NOT_EXIST_USER(1008, "존재하지 않는 유저입니다."),
    NOT_EXIST_ACCOUNT_BANK(1009, "존재하지 않는 은행정보 입니다."),

    //경매 관련
    INVALID_AUCTION(1100, "존재하지 않는 경매입니다."),
    ALREADY_TRADE_AUCTION(1101, "이미 거래 중인 경매입니다."),
    NOT_EXIST_BID(1102, "존재하지 않는 입찰입니다."),
    INVALID_BID_RANGE(1103, "입찰금의 범위가 잘못되었습니다."),
    INVALID_SUCCESS_BID_USER(1104, "유효하지 않은 입찰자입니다."),
    NOT_CONFIRMED_BID_AUCTION(1105, "낙찰 확정된 경매가 아닙니다."),
    INVALID_AUCTION_ORDER(1006, "존재하지 않는 경매 결제 내역입니다."),
    INVALID_AUCTION_ORDER_STATUS(1007, "잘못된 경매 검색 기준 정보입니다."),
    INVALID_HIGHEST_BIDDING(1008, "최고 입찰금보다 낮은 입찰금 입니다."),
    BID_EXCEEDS_UPPER_PRICE(1008, "상한가 보다 높은 입찰금 입니다."),

    //물물교환 관련
    INVALID_BARTER(1200, "존재하지 않는 물물교환입니다."),
    BARTER_NO_ITEM(1201, "해당 기프티콘이 존재하지 않습니다."),
    BARTER_NO_SELECT(1202, "기프티콘을 선택하지 않았습니다."),

    INVALID_BARTER_REQUEST(1203, "존재하지 않는 물물교환 교환요청입니다."),
    ALREADY_EXPIRED_BARTER(1204, "이미 완료 또는 만료된 물물교환입니다."),
    ALREADY_SEDED_REQUEST_BARTER(1205, "이미 요청을 보낸 물물교환입니다."),
    INVALID_BARTER_STATUS(1206, "적합하지 않은 물물교환 상태입니다."),


    //스토어 관련
    INVALID_STORE(1300, "존재하지 않는 판매입니다."),
    ALREADY_TRADE_STORE(1301, "이미 거래 중인 판매입니다."),

    //마이페이지 관련
    ALREADY_USE_GIFTICON(1400, "이미 사용된 기프티콘입니다."),
    ALREADY_REGIST_GIFTICON(1401, "이미 등록된 기프티콘입니다."),


    //알림 관련
    ALREADY_TRADE_BARTER(1500, "이미 거래된 물물교환입니다."),
    INVALID_NOTI_TYPE(1501, "적합하지 않은 알림 타입입니다."),

    //기프티콘 관련
    INVALID_GIFTICON(1600,"존재하지 않는 기프티콘입니다."),
    NOT_KEEP_GIFTICON(1601, "보관 중인 기프티콘이 아닙니다."),
    ALREADY_EXPIRED_GIFTICON(1602, "유효기간이 이미 지난 기프티콘 입니다."),
    NOT_STATUS_TRADE_GIFTICON(1603, "물물교환 중인 기프티콘이 아닙니다."),

    //문의 관련
    INVALID_INQUIRY(1700, "존재하지 않는 문의입니다."),

    //카테고리 관련
    INVALID_SUB_CATEGORY(1800, "존재하지 않는 소분류입니다."),
    INVALID_MAIN_CATEGORY(1801, "존재하지 않는 대분류입니다."),

    //거래 관련
    NOT_ENOUGH_CASH(1802, "잔액이 부족합니다.");

    private final Integer code;
    private final String message;
}
