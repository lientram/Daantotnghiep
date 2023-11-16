package com.ps20652.DATN.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ps20652.DATN.service.UploadService;

@Service
public class UploadServiceImpl implements UploadService {

	
	@Autowired
    private ResourceLoader resourceLoader;

	@Override
	public String uploadImage(MultipartFile image) throws IOException {
		String imageString = "Logo.png"; // Giá trị mặc định
        if (!image.isEmpty()) {
            // Lấy đường dẫn thực của dự án
            Resource resource = resourceLoader.getResource("classpath:/");
            String projectPath = resource.getFile().getAbsolutePath();

            // Đường dẫn lưu trữ hình ảnh
            String uploadPath = projectPath + "/static/assets/images/";
            Path path = Paths.get(uploadPath);

            // Tạo tên mới cho hình ảnh để tránh trùng lặp
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            String fileExtension = StringUtils.getFilenameExtension(fileName);
            String newFileName = System.currentTimeMillis() + "." + fileExtension;

            // Lưu trữ hình ảnh vào thư mục uploads với tên mới
            Files.copy(image.getInputStream(), path.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);

            // Lấy tên hình ảnh đã lưu
            imageString = newFileName;
        }
        return imageString;
	}
}
