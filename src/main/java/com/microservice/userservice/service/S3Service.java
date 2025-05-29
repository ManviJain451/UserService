package com.microservice.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String s3bucket;

    @Value("${aws.region}")
    private String region;

    private final S3Client s3Client;

    public String uploadFile(MultipartFile file, String username) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            }
            String key = "data/" + username + extension;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(s3bucket)
                    .key(key)
//                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            return "File uploaded successfully: " + key;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();
        }
    }


    public String getPublicFileUrl(String username) {
        String[] extensions = {".png", ".jpg", ".jpeg"};
        for (String ext : extensions) {
            String key = "data/" + username + ext;
            try {
                s3Client.headObject(HeadObjectRequest.builder()
                        .bucket(s3bucket)
                        .key(key)
                        .build());

                return String.format("https://%s.s3.%s.amazonaws.com/%s", s3bucket, region, key);
            } catch (S3Exception e) {
                if (e.statusCode() != 404) {
                    e.printStackTrace();
                    return "error checking image";
                }
            }
        }
        return "no image found";
    }


}
