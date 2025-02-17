package com.conseller.conseller.core.gifticon.domain;

import com.conseller.conseller.core.gifticon.implement.*;
import com.conseller.conseller.core.image.implement.ImageManager;
import com.conseller.conseller.core.gifticon.api.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.api.dto.request.RegisterGifticon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GifticonService {

    private final GifticonReader gifticonReader;
    private final GifticonValidator gifticonValidator;
    private final UsedGifticonAppender usedGifticonAppender;
    private final GifticonAppender gifticonAppender;
    private final GifticonFinder gifticonFinder;
    private final ImageManager  imageManager;

    @Transactional(readOnly = true)
    public GifticonResponse getGifticonResponse(long gifticonIdx) {
        return gifticonReader.read(gifticonIdx).toResponse();
    }

    @Transactional
    public void registGifticon(long userIdx, RegisterGifticon registerGifticon, MultipartFile originalFile, MultipartFile cropFile) throws IOException {
        gifticonValidator.isValidRegistGiftion(registerGifticon);

        String allImageUrl = imageManager.uploadFile(originalFile);
        String dataImageUrl = cropFile.isEmpty() ? null : imageManager.uploadFile(cropFile);

        gifticonAppender.append(Gifticon.of(registerGifticon, allImageUrl, dataImageUrl, userIdx));
    }

    @Transactional
    public void useGifticon(long gifticonIdx) {
        Gifticon gifticon = gifticonReader.read(gifticonIdx);
        usedGifticonAppender.append(new UsedGifticon(gifticon.getGifticonBarcode()));
    }

    public List<ExpiringGifticonResponse> getGifticonExpirationDates() {
        return gifticonFinder.getGifticonsExpiringCnt();
    }
}

