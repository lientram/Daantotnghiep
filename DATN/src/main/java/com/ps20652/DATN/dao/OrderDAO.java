package com.ps20652.DATN.dao;



import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.Revenue;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {

	Order findByUserAndStatus(Account user, String status);
	
	List<Order> findByUser_Username(String username);
	
	 List<Order> findByOrderDateBetween(Date startDate, Date endDate);
	
	 Order findByOrderId(Integer orderId);
	 
	 List<Order> findByStatus(String status);

	 Page<Order> findByUser_Username(String username, Pageable pageable);
	 
}
