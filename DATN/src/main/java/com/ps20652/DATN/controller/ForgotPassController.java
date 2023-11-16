package com.ps20652.DATN.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.service.AccountService;

@Controller
public class ForgotPassController {
    
    @Autowired
    AccountService accountService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "security2/forgot-password"; // Trả về tên của file HTML template cho quên mật khẩu
    }
    
   

    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, HttpSession session, Model model) {
        if (isEmailExists(email)) {
            accountService.sendOTP(email);
            session.setAttribute("email", email); // Lưu email vào session
            return "redirect:/enter-otp"; // Chuyển hướng sang trang nhập OTP khi OTP được gửi thành công
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy email hoặc có lỗi xãy ra");
            return "otp-sent-failed"; // Trả về trang thông báo OTP gửi thất bại
        }
    }

    @GetMapping("/enter-otp")
    public String showEnterOtp(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email"); // Lấy thông tin email từ session
        if (email != null) {
            // Sử dụng email ở đây cho mục đích hiển thị hoặc xử lý
            model.addAttribute("email", email);
            return "security2/enter-otp"; // Trả về trang nhập OTP
        } else {
            // Xử lý nếu không tìm thấy thông tin email
            return "redirect:/forgot-password"; // Chuyển hướng nếu không có email trong session
        }
    }
    
    
   

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam String email, HttpSession session, @RequestParam String otp, Model model) {
        if (accountService.verifyOTP(email, otp)) {
            session.setAttribute("email", email);
            session.setAttribute("otpVerified", "true"); // Đánh dấu rằng OTP đã được xác thực thành công
            return "redirect:/reset-password"; // Trả về trang đặt lại mật khẩu khi OTP xác thực thành công
        } else {
            model.addAttribute("errorMessage", "Mã OTP sai hoặc đã vô hiệu");
            return "verify-otp-failed"; // Trả về trang thông báo xác thực OTP thất bại
        }
    }

    
    @GetMapping("/reset-password")
    public String showResetpassword(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email"); // Lấy thông tin email từ session
        String otpVerified = (String) session.getAttribute("otpVerified"); // Lấy thông tin đã xác thực OTP từ session

        if (email != null && otpVerified != null && otpVerified.equals("true")) {
            // Sử dụng email ở đây cho mục đích hiển thị hoặc xử lý
            model.addAttribute("email", email);
            return "security2/reset-password"; // Trả về trang nhập OTP
        } else {
            // Xử lý nếu không tìm thấy thông tin email hoặc chưa xác thực OTP
            return "redirect:/enter-otp"; // Chuyển hướng nếu chưa xác thực OTP
        }
    }

    

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword, Model model) {
        boolean passwordResetSuccessful = accountService.resetPassword(email, newPassword);
        if (passwordResetSuccessful) {
            return "redirect:/login"; // Trả về trang thông báo đặt lại mật khẩu thành công
        } else {
            model.addAttribute("errorMessage", "Đặt lại pass thất bại");
            return "reset-password-failed"; // Trả về trang thông báo đặt lại mật khẩu thất bại
        }
    }

    private boolean isEmailExists(String email) {
        Account account = accountService.findByEmail(email);
        return account != null;
    }
}





