package com.conseller.conseller.global.security.implement;

import com.conseller.conseller.global.entity.BlackListEntity;
import com.conseller.conseller.global.security.infrastructure.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlackListManager {

    private final BlackListRepository blackListRepository;

    public void addBlackList(String token, long userIdx) {
        blackListRepository.save(BlackListEntity.of(token, userIdx));
    }
}
