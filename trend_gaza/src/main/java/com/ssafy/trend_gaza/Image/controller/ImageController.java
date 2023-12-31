package com.ssafy.trend_gaza.Image.controller;


import com.ssafy.trend_gaza.Image.dto.ImagesResponse;
import com.ssafy.trend_gaza.Image.service.UploadService;
import com.ssafy.trend_gaza.util.AuthenticationUtil;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/images")
@CrossOrigin("*")
public class ImageController {

    private final UploadService uploadService;

    public ImageController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public ResponseEntity<ImagesResponse> uploadImage(
            @RequestPart final List<MultipartFile> images) {
        String loginUserId = AuthenticationUtil.getCurrentUserSocialId();
        final ImagesResponse imagesResponse = uploadService.save(images, loginUserId);
        final String firstImageName = imagesResponse.getImageNames().get(0);
        return ResponseEntity.created(URI.create(firstImageName)).body(imagesResponse);
    }
}
