package com.ps20652.DATN.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.StockHistoryDAO;
import com.ps20652.DATN.dao.VoucherDAO;
import com.ps20652.DATN.entity.StockHistory;
import com.ps20652.DATN.entity.Voucher;
import com.ps20652.DATN.service.HistoryStockService;
import com.ps20652.DATN.service.VoucherService;

@Service
public class HistoryStockServiceImpl implements HistoryStockService {

	@Autowired
	StockHistoryDAO stockHistoryDAO;
	
	@Override
	public List<StockHistory> findAll() {

		return stockHistoryDAO.findAll();
	}

   
}