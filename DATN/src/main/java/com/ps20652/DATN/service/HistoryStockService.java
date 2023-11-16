package com.ps20652.DATN.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.StockHistory;


public interface HistoryStockService {

	
	public List<StockHistory> findAll();


	
	

	
}
