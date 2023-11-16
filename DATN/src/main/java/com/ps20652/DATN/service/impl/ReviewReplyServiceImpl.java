package com.ps20652.DATN.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.CustomerFeedbackDAO;
import com.ps20652.DATN.dao.ReviewReplyDAO;
import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.entity.ReviewReply;
import com.ps20652.DATN.service.FeedbackService;
import com.ps20652.DATN.service.ReviewReplyService;

@Service
public class ReviewReplyServiceImpl implements ReviewReplyService {

	@Autowired
	private ReviewReplyDAO reviewReplyDAO;

	@Override
	public List<ReviewReply> findAll() {
		return reviewReplyDAO.findAll();
	}
	@Override
	public ReviewReply create(ReviewReply reviewReply) {
		return reviewReplyDAO.save(reviewReply);
	}

	@Override
	public ReviewReply findbyId(Integer id) {
		return reviewReplyDAO.findById(id).get();
	}

	@Override
	public void delete(ReviewReply id) {
		reviewReplyDAO.delete(id);
	}
	@Override
	public List<ReviewReply> findByFeedbackId(int feedbackId) {
		return reviewReplyDAO.findByCustomerFeedbackFeedbackId(feedbackId);
	}



	
	

}
