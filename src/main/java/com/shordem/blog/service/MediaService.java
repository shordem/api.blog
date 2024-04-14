package com.shordem.blog.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MediaService {

    private final AwsS3Service awsS3Service;

    public String uploadFile(MultipartFile file) throws AmazonServiceException, IOException {
        return awsS3Service.uploadFile(file);
    }
}
