package com.shordem.blog.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class AwsS3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsS3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${shordem.aws.s3.bucketName}")
    private String bucket;

    @Value("${shordem.aws.baseFolder}")
    private String baseFolder;

    @Async
    public String uploadFile(MultipartFile multipartFile) throws IOException, AmazonServiceException {
        LOGGER.info("File upload in progress.");
        File file = convertMultiPartFileToFile(multipartFile);

        try {
            String fileKey = uploadFileToS3(bucket, file);
            LOGGER.info("File upload is completed.");
            file.delete();
            return fileKey;
        } catch (AmazonServiceException ex) {
            LOGGER.info("File upload is failed.");
            LOGGER.error("Error= {} while uploading file.", ex.getMessage());
            file.delete();

            throw ex;
        }

    }

    private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            LOGGER.error("Error converting multipartFile to file", e.getMessage());

            throw e;
        }

        return file;
    }

    private String uploadFileToS3(String bucket, File file) {
        String fileExt = StringUtils.getFilenameExtension(file.getName());
        String key = UUID.randomUUID().toString() + "." + fileExt;
        LOGGER.info("Uploading file with name= " + key);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, baseFolder + "/" + key, file);
        s3Client.putObject(putObjectRequest);
        return key;
    }

}
