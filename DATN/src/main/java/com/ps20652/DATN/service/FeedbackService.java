package com.ps20652.DATN.service;

import java.util.List;

import com.ps20652.DATN.entity.CustomerFeedback;


public interface FeedbackService {

	
	public List<CustomerFeedback> findAll();
	
	public CustomerFeedback create(CustomerFeedback customerFeedback);

	public CustomerFeedback findbyId(Integer id);
	
	List<CustomerFeedback> findByUserId(Integer userId);
	
	void delete(CustomerFeedback id);
	
	public CustomerFeedback findbyIdFb(CustomerFeedback customerFeedback);
	
	List<CustomerFeedback> findByProductProductId(int feedbackId);
}
