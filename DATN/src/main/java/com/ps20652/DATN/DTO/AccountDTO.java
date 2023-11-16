package com.ps20652.DATN.DTO;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AccountDTO {
    private int userId;
    private String username;
    private Integer phonenumber;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String role = "USER";
    private MultipartFile photo;
    private String otp;
    private LocalDateTime otpCreatedAt;

}