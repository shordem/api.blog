package com.shordem.blog.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.shordem.blog.payload.response.MediaResponse;
import com.shordem.blog.service.MediaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media/")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(value = "/upload/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doUploadFile(@RequestParam(required = true, name = "file") MultipartFile file)
            throws AmazonServiceException, IOException {

        String fileKey = mediaService.uploadFile(file);

        MediaResponse response = new MediaResponse(fileKey);

        return ResponseEntity.ok().body(response);
    }

}
