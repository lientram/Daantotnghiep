package com.ps20652.DATN.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.entity.Product;

public interface CustomerFeedbackDAO extends JpaRepository<CustomerFeedback, Integer> {

	List<CustomerFeedback> findByCustomerUserId(Integer customer);
	
	List<CustomerFeedback> findByProductProductId(int feedback_id);
	
//	 List<CustomerFeedback> findByOrderIdAndUserId(Integer orderId, Integer userId);
	
}
