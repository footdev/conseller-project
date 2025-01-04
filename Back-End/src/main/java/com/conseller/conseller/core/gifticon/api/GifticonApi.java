package com.conseller.conseller.core.gifticon.api;

import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.api.dto.request.RegisterGifticon;
import com.conseller.conseller.core.gifticon.domain.GifticonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gifticon")
public class GifticonApi {

    private final GifticonService gifticonService;

    @PostMapping(value = "/{userIdx}", consumes = "multipart/form-data")
    public ResponseEntity<Void> registGifticon(
            @PathVariable long userIdx,
            @RequestPart(name = "gifticonPostRequest") RegisterGifticon gifticonRequest,
            @RequestPart(name = "originalFile") MultipartFile originalFile,
            @RequestPart(name = "cropFile") MultipartFile cropFile
    ) throws IOException {
        gifticonService.registGifticon(userIdx, gifticonRequest.toRegisterGifticon(), originalFile, cropFile);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/{gifticonIdx}")
    public ResponseEntity<GifticonResponse> getGifticon(@PathVariable long gifticonIdx) {
        return ResponseEntity.ok()
                .body(gifticonService.getGifticonResponse(gifticonIdx));
    }

    @DeleteMapping("/{gifticonIdx}")
    public ResponseEntity<Void> useGifticon(@PathVariable long gifticonIdx) {
        gifticonService.useGifticon(gifticonIdx);
        return ResponseEntity.ok()
                .build();
    }
}
