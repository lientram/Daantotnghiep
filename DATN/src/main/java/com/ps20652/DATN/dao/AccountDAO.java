package com.ps20652.DATN.dao;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ps20652.DATN.entity.Account;



@Repository
public interface AccountDAO extends JpaRepository<Account, Integer> {
	Account findByUsername(String username);
	 
	 Account findByEmail(String email);
	 
	 boolean existsByUsername(String username);
	 
	 boolean existsByEmail(String email);
	 
}