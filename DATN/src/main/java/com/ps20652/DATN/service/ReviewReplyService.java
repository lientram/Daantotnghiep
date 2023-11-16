package com.ps20652.DATN.service;

import java.util.List;

import com.ps20652.DATN.entity.ReviewReply;

public interface ReviewReplyService {

	
	public List<ReviewReply> findAll();
	
	public ReviewReply create(ReviewReply reviewReply);

	public ReviewReply findbyId(Integer id);
	
//	List<ReviewReply> findByUserId(Integer userId);
	
	List<ReviewReply> findByFeedbackId(int feedbackId);
	
	void delete(ReviewReply id);
	

}
