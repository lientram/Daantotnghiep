package com.ps20652.DATN.dao;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ps20652.DATN.entity.Revenue;

@Repository
public interface RevenueDAO extends JpaRepository<Revenue, Integer> {

	void deleteByOrder_OrderId(Integer orderId);
	
}
