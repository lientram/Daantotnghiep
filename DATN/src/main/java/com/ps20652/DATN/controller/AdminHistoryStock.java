package com.ps20652.DATN.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ps20652.DATN.entity.StockHistory;
import com.ps20652.DATN.service.HistoryStockService;


@Controller
@RequestMapping("admin/history")
public class AdminHistoryStock {
	
	@Autowired
	HistoryStockService historyService;

	@GetMapping
	public String showStockHistory(Model model) {
	    // Lấy lịch sử nhập hàng cho sản phẩm
	    List<StockHistory> stockHistoryList = historyService.findAll();
	    model.addAttribute("stockHistoryList", stockHistoryList);
	    return "AdminCpanel/stock-history";
	}
	
}
