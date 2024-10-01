package com.conseller.conseller.gifticon.api;

import com.conseller.conseller.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.gifticon.api.dto.request.GifticonRegisterRequest;
import com.conseller.conseller.gifticon.domain.GifticonService;
import com.conseller.conseller.image.infrastructure.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gifticon")
public class GifticonApi {

    private final GifticonService gifticonService;
    private final ImageRepository imageRepository;

    @PostMapping(value = "/{userIdx}", consumes = "multipart/form-data")
    public ResponseEntity<Void> registGifticon(
            @PathVariable long userIdx,
            @RequestPart(name = "gifticonPostRequest") GifticonRegisterRequest gifticonRequest,
            @RequestPart(name = "originalFile") MultipartFile originalFile,
            @RequestPart(name = "cropFile") MultipartFile cropFile
    ) throws IOException {

        log.info("기프티콘 등록 호출");

        //validation 클래스로 검증 후 사용하거나 유효기간이 지났으면 예외 처리


        String allImageUrl = imageRepository.uploadFile(originalFile);
        String dataImageUrl = null;

        if (!cropFile.isEmpty()) {
            dataImageUrl = imageRepository.uploadFile(cropFile);
        }

        gifticonService.registGifticon(userIdx, gifticonRequest, allImageUrl, dataImageUrl);

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/{gifticonIdx}")
    public ResponseEntity<GifticonResponse> getGifticon(@PathVariable long gifticonIdx) {

        log.info("기프티콘 조회 호출");

        return ResponseEntity.ok()
                .body(gifticonService.getGifticonResponse(gifticonIdx));
    }

    @DeleteMapping("/{gifticonIdx}")
    public ResponseEntity<Void> deleteGifticon(@PathVariable long gifticonIdx) {

        log.info("기프티콘 삭제 호출");

        //기프티콘 엔티티를 삭제하고 url을 리턴한다.
        gifticonService.deleteGifticon(gifticonIdx);

        log.info("기프티콘 삭제 완료");

        return ResponseEntity.ok()
                .build();
    }
}
