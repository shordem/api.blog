package com.shordem.blog.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<MediaResponse> doUploadFile(@RequestParam(required = true, name = "file") MultipartFile file)
            throws AmazonServiceException, IOException {

        String fileKey = mediaService.uploadFile(file);

        MediaResponse response = new MediaResponse(fileKey);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{fileKey}/")
    public ResponseEntity<byte[]> doGetFile(@PathVariable String fileKey) throws IOException {
        byte[] fileData = mediaService.getFile(fileKey);
        HttpHeaders headers = new HttpHeaders();

        String[] fileKeySplit = fileKey.split("\\.");
        String fileExt = fileKeySplit[fileKeySplit.length - 1];

        MediaType mediaType = getMediaType(fileExt);
        headers.setContentType(mediaType);

        return ResponseEntity.ok().headers(headers).body(fileData);

    }

    private MediaType getMediaType(String fileExt) {
        switch (fileExt.toLowerCase()) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpeg":
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.IMAGE_JPEG; // or any other default media type you prefer
        }
    }

}
