package com.conseller.conseller.global.utils;

import com.conseller.conseller.core.auction.infrastructure.AuctionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderNumberGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String RANDOM_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int RANDOM_PART_LENGTH = 5;
    private final SecureRandom secureRandom;
    private final AuctionOrderRepository orderRepository;

    public OrderNumberGenerator(SecureRandom secureRandom, AuctionOrderRepository orderRepository) {
        this.secureRandom = secureRandom;
        this.orderRepository = orderRepository;
    }

    /**
     * 주문 번호를 생성합니다. 유일성을 보장하기 위해 중복 확인을 수행합니다.
     *
     * @return 생성된 유일한 주문 번호
     */
    public String generateUniqueOrderNumber() {
        String orderNumber;
        do {
            orderNumber = generateOrderNumber();
        } while (orderRepository.existsByOrderNumber(orderNumber));
        return orderNumber;
    }

    private String generateOrderNumber() {
        String datePart = generateDatePart();
        String randomPart = generateRandomPart(RANDOM_PART_LENGTH);
        return datePart + randomPart;
    }

    private String generateDatePart() {
        return LocalDateTime.now().format(DATE_FORMATTER);
    }

    private String generateRandomPart(int length) {
        StringBuilder randomStringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(RANDOM_CHARACTERS.length());
            randomStringBuilder.append(RANDOM_CHARACTERS.charAt(index));
        }
        return randomStringBuilder.toString();
    }
}
