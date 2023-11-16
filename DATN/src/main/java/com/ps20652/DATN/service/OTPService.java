package com.ps20652.DATN.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.AccountDAO;
import com.ps20652.DATN.entity.Account;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    String abcOTP;

    @Autowired
    AccountDAO accdao;

    @Autowired
    JavaMailSender javamail;
    private final Map<String, String> otpCache = new ConcurrentHashMap<>(); // Lưu trữ OTP tạm thời

    public String generateOTP() {
        // Tạo OTP ngẫu nhiên
        String otp = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        return otp;
    }

    public void saveOTP(String email, String otp) {
        otpCache.put(email, otp);
    }

    public String getOTP(String email) {
        return otpCache.get(email);
    }

    public boolean verifyOTP(String email, String otp) {

        String storedOTP = otpCache.get(email);

        if (abcOTP != null && abcOTP.equals(otp)) {
            // Xóa OTP sau khi xác thực thành công
            otpCache.remove(email);
            return true;
        }

        return false;
    }

    // public void sendOTP(String userEmail) {
    // Account account = accdao.findByEmail(userEmail);
    // if (account == null) {
    // // Xử lý trường hợp người dùng không tồn tại
    // // Thông báo lỗi hoặc thực hiện xử lý tùy thuộc vào yêu cầu của bạn
    // return;
    // }
    //
    // // Tạo mã OTP
    // String otp = generateOTP();
    //
    // // Lưu mã OTP và thời gian tạo vào cơ sở dữ liệu
    // account.setOtp(otp);
    // account.setOtpCreatedAt(LocalDateTime.now()); // Lưu thời gian tạo mã OTP
    // accdao.save(account);
    //
    // // Gửi email chứa mã OTP
    // sendOTPtoMail(userEmail, otp);
    // }
    public Account sendOTP(String userEmail) {
        Account account = accdao.findByEmail(userEmail);

        if (account == null) {
            // Tạo một tài khoản mới với email đã cung cấp
            Account newAccount = new Account();
            newAccount.setEmail(userEmail);

            // Gán mã OTP cho tài khoản mới
            String otp = generateOTP();
            abcOTP = otp;
            newAccount.setOtp(otp);
            newAccount.setOtpCreatedAt(LocalDateTime.now());

            // Lưu tài khoản mới vào cơ sở dữ liệu

            // Gửi email chứa mã OTP cho người dùng mới
            sendOTPtoMail(userEmail, otp);
            return newAccount;
        } else {
            String otp = generateOTP();
            account.setOtp(otp);
            account.setOtpCreatedAt(LocalDateTime.now()); // Lưu thời gian tạo mã OTP
            accdao.save(account);

            // Gửi email chứa mã OTP
            sendOTPtoMail(userEmail, otp);
            return null;
        }
    }

    private void sendOTPtoMail(String userEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("DATN Mã OTP");
        message.setText("Mã OTP của bạn là: " + otp);

        javamail.send(message);
    }

}
// Tạo mã OTP cho tài khoản đã tồn tại