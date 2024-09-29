package com.conseller.conseller.utils.dummy;

import com.conseller.conseller.utils.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dummy")
@RequiredArgsConstructor
public class DummyController {

    private final DummyService dummyService;

    @PostMapping("/users")
    public ResponseEntity<Object> createDummyUser() {
        dummyService.createDummyUsersAndSaveAll();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/gifticons")
    public ResponseEntity<Object> createDummyGifticon() {
        dummyService.createDummyGifticonsAndSaveAll();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auctions")
    public ResponseEntity<Object> createDummyAuctions() {
        dummyService.createDummyAuctionsAndSaveAll();
        return ResponseEntity.ok().build();
    }
}
