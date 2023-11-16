package com.ps20652.DATN.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.ps20652.DATN.entity.Account;


public interface AccountService {

	
	public List<Account> findAll();

	public Account create(Account acc);
	
	public Account findbyId(Integer id);
	
	public Account findbyIdAcc(Account account);
	
	public void delete(Account account);
	
	Account update(Account account);

	Account findByUsername(String username);
	 
	public void sendOTP(String userEmail);
	
	Account findByEmail(String email);
	
	public boolean verifyOTP(String email, String otp);
	
	public boolean resetPassword(String email, String newPassword);
	
	boolean isUserExists(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
	
	

	
}