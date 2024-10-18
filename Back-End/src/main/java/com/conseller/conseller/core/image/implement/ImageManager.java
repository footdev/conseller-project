package com.conseller.conseller.core.image.implement;

import com.conseller.conseller.core.image.infrastructure.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageManager {

    private final ImageRepository imageRepository;

    public String uploadFile(MultipartFile file) throws IOException {
        return imageRepository.uploadFile(file);
    }
}
