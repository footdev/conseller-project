package com.conseller.conseller.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeConverter {

    public static LocalDateTime convertDateTime(String date) {

        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDate localDate = LocalDate.parse(date, formatter);
            return localDate.atTime(23, 59, 59);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static LocalDateTime convertLocalDateTime(String dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            return LocalDateTime.parse(dateTime, formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertString(LocalDateTime dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            return dateTime.format(formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Timestamp convertTimestamp(String date) {
        try {
            LocalDateTime localDateTime = convertLocalDateTime(date);
            return Timestamp.valueOf(localDateTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static LocalDateTime generateRandomDate() {
        return LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(365));
    }

    //현재 날짜 ~ 약 10개월 전 까지의 일 단위의 날짜를 랜덤하게 생성
    public static LocalDateTime generateRandomDate10Month() {
        return LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(300));
    }
}