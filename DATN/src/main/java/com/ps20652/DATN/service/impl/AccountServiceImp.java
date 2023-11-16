package com.ps20652.DATN.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.AccountDAO;
import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.service.AccountService;

@Service
public class AccountServiceImp implements AccountService {

	@Autowired

	private AccountDAO accDAO;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<Account> findAll() {
		return accDAO.findAll();
	}

	@Override
	public Account create(Account account) {
		// Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
		account.setPassword(passwordEncoder.encode(account.getPassword()));

		// Lưu thông tin tên người dùng và mật khẩu vào cơ sở dữ liệu
		account.setUsername(account.getUsername()); // Thay "getUsername()" bằng phương thức lấy tên người dùng từ đối
													// tượng account
		account.setPassword(account.getPassword()); // Sử dụng mã hóa mật khẩu

		return accDAO.save(account);
	}

	@Override
	public Account findbyId(Integer id) {

		return accDAO.findById(id).get();
	}

	@Override
	public void delete(Account account) {
		accDAO.delete(account);

	}

	@Override
	public Account update(Account account) {

		return accDAO.save(account);
	}

	@Override
	public void sendOTP(String userEmail) {
		Account account = accDAO.findByEmail(userEmail);
		if (account == null) {
			// Xử lý trường hợp người dùng không tồn tại
			// Thông báo lỗi hoặc thực hiện xử lý tùy thuộc vào yêu cầu của bạn
			return;
		}

		// Tạo mã OTP
		String otp = generateOTP();

		// Lưu mã OTP và thời gian tạo vào cơ sở dữ liệu
		account.setOtp(otp);
		account.setOtpCreatedAt(LocalDateTime.now()); // Lưu thời gian tạo mã OTP
		accDAO.save(account);

		// Gửi email chứa mã OTP
		sendOTPtoMail(userEmail, otp);
	}

	private void sendOTPtoMail(String userEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(userEmail);
		message.setSubject("DATN Mã OTP");
		message.setText("Mã OTP của bạn là: " + otp);

		javaMailSender.send(message);
	}

	public String generateOTP() {
		// Tạo UUID ngẫu nhiên
		UUID uuid = UUID.randomUUID();

		// Lấy giá trị thập phân của UUID (loại bỏ dấu gạch nối và ký tự)
		String uuidStr = uuid.toString().replaceAll("-", "").replaceAll("[a-zA-Z]", "");

		// Lấy 6 ký tự đầu tiên của UUID
		String otp = uuidStr.substring(0, 6);

		return otp;
	}

	@Override
	public Account findByEmail(String email) {
		return accDAO.findByEmail(email);
	}

	@Override
	public boolean verifyOTP(String email, String otp) {
		Account account = accDAO.findByEmail(email);

		if (account != null) {
			LocalDateTime otpCreatedAt = account.getOtpCreatedAt();
			LocalDateTime currentTime = LocalDateTime.now();
			Duration duration = Duration.between(otpCreatedAt, currentTime);

			// Kiểm tra xem đã qua 1 phút chưa
			if (duration.getSeconds() <= 60) {
				String storedOTP = account.getOtp();

				// So sánh mã OTP người dùng nhập với mã OTP đã lưu
				if (otp.equals(storedOTP)) {
					return true; // Mã OTP hợp lệ
				}
			}
		}

		return false; // Mã OTP không hợp lệ hoặc hết hạn
	}

	public String getUserOTPFromDatabase(String email) {
		// Sử dụng Spring Data JPA để lấy thông tin người dùng từ cơ sở dữ liệu
		Account account = accDAO.findByEmail(email);
		if (account != null) {
			return account.getOtp(); // Lấy mã OTP từ đối tượng User
		} else {
			return null; // Hoặc null nếu không tìm thấy người dùng
		}
	}

	@Override
	public boolean resetPassword(String email, String newPassword) {
		// Thực hiện việc đặt lại mật khẩu cho người dùng với mật khẩu mới
		// Ví dụ: Lưu mật khẩu mới vào cơ sở dữ liệu
		boolean passwordResetSuccessful = saveNewPassword(email, newPassword);

		return passwordResetSuccessful;
	}

	public boolean saveNewPassword(String email, String newPassword) {
		Account account = accDAO.findByEmail(email);

		if (account != null) {
			// Mã hóa mật khẩu sử dụng PasswordEncoder
			String encodedPassword = passwordEncoder.encode(newPassword);

			// Cập nhật mật khẩu đã được mã hóa vào cơ sở dữ liệu
			account.setPassword(encodedPassword);
			accDAO.save(account);

			return true;
		}

		return false;
	}

	@Override
	public Account findbyIdAcc(Account account) {
		return accDAO.findById(account.getUserId()).get();
	}

	@Override
	public Account findByUsername(String username) {

		return accDAO.findByUsername(username);
	}

	@Override
	public boolean isUserExists(String username) {
		return accDAO.existsByUsername(username);
	}

	@Override
	public boolean existsByUsername(String username) {
		return accDAO.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return accDAO.existsByEmail(email);
	}

}