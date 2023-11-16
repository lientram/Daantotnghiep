package com.ps20652.DATN.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ps20652.DATN.entity.StockHistory;

public interface StockHistoryDAO extends JpaRepository<StockHistory, Integer> {
 
}