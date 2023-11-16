package com.ps20652.DATN.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadImage(MultipartFile image) throws IOException;
}
