package com.conseller.conseller.core.image.api;

import com.conseller.conseller.core.image.implement.ImageManager;
import com.conseller.conseller.core.image.infrastructure.ImageRepository;
import com.conseller.conseller.core.user.domain.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageApi {

    private final ImageManager imageManager;
    private final UserService userService;

    @PostMapping("/{userIdx}/profile")
    public ResponseEntity<Void> uploadUserProfile(@PathVariable Long userIdx, @RequestPart("file") MultipartFile file) throws IOException {
        String url = imageManager.uploadFile(file);
        userService.uploadProfile(userIdx, url);
        return ResponseEntity.ok().build();
    }

}
