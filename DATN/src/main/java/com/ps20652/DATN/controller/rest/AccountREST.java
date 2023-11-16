package com.ps20652.DATN.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps20652.DATN.dao.AccountDAO;
import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.service.AccountService;

@RestController
@RequestMapping("/api")
public class AccountREST {

	@Autowired 
	private AccountService a_service;
	
	
	@GetMapping("/account")
	public List<Account> getAccounts() {
		return a_service.findAll();
	}
	@PostMapping("/account/create")
	public Account createAcc(@RequestBody Account acc) {
		return a_service.create(acc);
	}
	@GetMapping("/account/{id}")
	public Account getOne(@PathVariable Integer id) {
		return a_service.findbyId(id);
	}
	@PutMapping("/account/{id}")
	public Account update(@RequestBody Account account,@PathVariable("id") Integer id) {
		return a_service.update(account);
	}
	@DeleteMapping("/account/delete/{id}")
	public void delete(@PathVariable("id")Account id) {
		a_service.delete(id);
	}
	@GetMapping("/account/searchByUsername")
	    public Account searchUsername(@RequestParam String username) {
	        return a_service.findByUsername(username);
    }
	 @PostMapping("account/forgot-password")
	    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
			if(isEmailExists(email)) {
				 a_service.sendOTP(email);
			        return ResponseEntity.ok("Mã OTP đã gửi tới email.");
			}else {
				return ResponseEntity.badRequest().body("Không tìm thấy email hoặc có lỗi xãy ra");
			}  
	    }
	
	 private boolean isEmailExists(String email) {
		 Account account = a_service.findByEmail(email);
		    return account != null;
		}
	 
	 @PostMapping("account/verify-otp")
	    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {
	        if (a_service.verifyOTP(email, otp)) {
	            // Xác thực thành công, cho phép đặt lại mật khẩu
	            return ResponseEntity.ok("Mã OTP chính xác, có thể đặt lại mật khẩu ");
	        } else {
	            // Xác thực thất bại
	            return ResponseEntity.badRequest().body("Mã OTP sai hoặc đã vô hiệu ");
	        }
	    }
	 @PostMapping("account/reset-password")
	    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
	        // Thực hiện việc đặt lại mật khẩu cho người dùng với mật khẩu mới
	        boolean passwordResetSuccessful = a_service.resetPassword(email, newPassword);

	        if (passwordResetSuccessful) {
	            return ResponseEntity.ok("Đặt lại pass thành công");
	        } else {
	            return ResponseEntity.badRequest().body("Đặt lại pass thất bại");
	        }
	    }
}
