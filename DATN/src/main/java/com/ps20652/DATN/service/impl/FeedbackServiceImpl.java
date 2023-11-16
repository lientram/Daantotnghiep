package com.ps20652.DATN.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.CustomerFeedbackDAO;
import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private CustomerFeedbackDAO feedbackDAO;
	
	@Override
	public List<CustomerFeedback> findAll() {
		return feedbackDAO.findAll() ;
	}

	@Override
	public CustomerFeedback create(CustomerFeedback customerfeedback) {
		return feedbackDAO.save(customerfeedback);
	}

	@Override
	public CustomerFeedback findbyId(Integer id) {
		return feedbackDAO.findById(id).get();
	}

	@Override
	public List<CustomerFeedback> findByUserId(Integer userId) {
	
		return feedbackDAO.findByCustomerUserId(userId);
	}
	@Override
	public void delete(CustomerFeedback id) {
		feedbackDAO.delete(id);
	}

	@Override
	public CustomerFeedback findbyIdFb(CustomerFeedback customerFeedback) {
		return feedbackDAO.findById(customerFeedback.getFeedbackId()).get();
	}

	@Override
	public List<CustomerFeedback> findByProductProductId(int feedback_id) {
		return feedbackDAO.findByProductProductId(feedback_id);
	}

}
