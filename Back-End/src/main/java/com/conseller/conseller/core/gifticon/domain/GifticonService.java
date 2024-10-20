package com.conseller.conseller.core.gifticon.domain;

import com.conseller.conseller.core.gifticon.implement.GifticonAppender;
import com.conseller.conseller.core.gifticon.implement.GifticonFinder;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.core.gifticon.implement.UsedGifticonAppender;
import com.conseller.conseller.core.image.implement.ImageManager;
import com.conseller.conseller.core.gifticon.api.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.api.dto.request.GifticonRegisterRequest;
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
        return gifticonReader.readGifticon(gifticonIdx).toResponse();
    }

    @Transactional
    public void registGifticon(long userIdx, GifticonRegisterRequest gifticonRegisterRequest, MultipartFile originalFile, MultipartFile cropFile) throws IOException {
        gifticonValidator.isValidGiftion(gifticonRegisterRequest);

        String allImageUrl = imageManager.uploadFile(originalFile);
        String dataImageUrl = null;

        if (!cropFile.isEmpty()) {
            dataImageUrl = imageManager.uploadFile(cropFile);
        }
        gifticonAppender.append(Gifticon.of(gifticonRegisterRequest, allImageUrl, dataImageUrl, userIdx));
    }

    @Transactional
    public void deleteGifticon(long gifticonIdx) {
        Gifticon gifticon = gifticonReader.readGifticon(gifticonIdx);
        usedGifticonAppender.append(new UsedGifticon(gifticon.getGifticonBarcode()));
    }

    public List<ExpiringGifticonResponse> getGifticonExpirationDates() {
        return gifticonFinder.getGifticonsExpiringCnt();
    }
}

