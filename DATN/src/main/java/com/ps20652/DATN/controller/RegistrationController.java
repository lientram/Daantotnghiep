package com.ps20652.DATN.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.OTPService;

@Controller
@RequestMapping("/")
public class RegistrationController {

	@Autowired
	private AccountService userService;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
	    model.addAttribute("user", new Account());
	    model.addAttribute("registrationError", ""); // Đảm bảo trường lỗi được truyền vào giao diện
	    return "security2/register";
	}
	
	@GetMapping("/checkUsername")
	@ResponseBody
	public String checkUsernameExists(@RequestParam("username") String username) {
	    boolean exists = userService.existsByUsername(username);
	    return String.valueOf(exists);
	}



	@Autowired
	private OTPService otpService;

//	@PostMapping("/register")
//	public String registerUser(@ModelAttribute("user") Account user, Model model, @RequestParam("email") String email,
//	                            @RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
	    // Kiểm tra username đã tồn tại

//	    
//	    // Kiểm tra email đã tồn tại

//
//	    // Kiểm tra độ dài password

//
//	    // Tiếp tục xử lý nếu không có lỗi
//	    // ...
//
//	    // Chuyển hướng đến trang xác thực OTP
//	    return "redirect:/verifyOTP?email=" + email;
//	}
	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") Account user, Model model, @RequestParam("email") String email,
	        @RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
	    user.setUsername(username);
	    user.setPassword(password);
	    // ... (tạo OTP, gửi email)
	    String otp = otpService.generateOTP();
	    otpService.saveOTP(email, otp);
	    
	    // Kiểm tra acc để đảm bảo không null trước khi sử dụng
	    Account acc = otpService.sendOTP(email);
	    if (acc != null) {
	        user.setEmail(acc.getEmail());
	        user.setOtp(acc.getOtp());
	        user.setOtpCreatedAt(acc.getOtpCreatedAt());
	    } else {
	    	
	        // Xử lý trường hợp acc là null ở đây nếu cần thiết
	    }
	    if (userService.existsByUsername(username)) {model.addAttribute("registrationError", "Username đã tồn tại!");
	        return "security2/register";
	    }
	    if (userService.existsByEmail(email)) {
	        model.addAttribute("registrationError", "Email đã được đăng ký!");
	        return "security2/register";
	    }
	    
	    if (password.length() < 8) {
	        model.addAttribute("registrationError", "Password phải có ít nhất 8 ký tự!");
	        return "security2/register";
	    }
	    
	    userService.create(user);

	    // Lưu thông tin người dùng đã đăng ký vào session
	    session.setAttribute("registeredUser", user);
	    

	    // Chuyển hướng đến trang xác thực OTP
	    return "redirect:/verifyOTP?email=" + email; // Sửa user.getEmail() thành email
	}







	@PostMapping("/verifyOTP")
	public String verifyOTP(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model, HttpSession session) {
	    if (otpService.verifyOTP(email, otp)) {
	        Account registeredUser = (Account) session.getAttribute("registeredUser");
	        if (registeredUser != null) {
	            // Xác thực thành công, lưu thông tin người dùng vào session và chuyển hướng đến trang login
	            Account authenticatedUser = userService.findByEmail(email);
	            session.setAttribute("user", authenticatedUser);
	            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập
	        }
	    }
	    // Xác thực không thành công, thông báo lỗi
	    model.addAttribute("error", "Mã OTP không hợp lệ hoặc đã hết hạn.");
	    return "security/verifyOTP"; // Trở lại trang xác thực OTP
	}



	@GetMapping("/verifyOTP")
	public String showVerifyOTPPage(@RequestParam("email") String email, Model model) {
		model.addAttribute("email", email);
		return "security/verifyOTP"; // Trả về trang xác thực OTP
	}

}