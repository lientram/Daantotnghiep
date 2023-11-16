package com.ps20652.DATN.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Category;
import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.entity.ReviewReply;
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.FeedbackService;
import com.ps20652.DATN.service.ReviewReplyService;

@RestController
public class ReviewReplyREST {

	@Autowired 
	private ReviewReplyService rv_service;
	
    @Autowired
    private AccountService a_service;

    @Autowired
    private FeedbackService fb_service;
	    
	    
	@GetMapping("/getReviewReply")
	public List<ReviewReply> getFeedback() {
		return rv_service.findAll();
	}
	
	
	
	  @PostMapping("/reply/create")
	    public ReviewReply createReply(@RequestBody ReviewReply reply) throws Exception {
	        // Tìm người dùng và đánh giá liên quan
	        Account account = a_service.findbyIdAcc(reply.getCustomer());
	        CustomerFeedback feedback = fb_service.findbyIdFb(reply.getCustomerFeedback());

	        if (account != null && feedback != null) {
	            reply.setCustomer(account);
	            reply.setCustomerFeedback(feedback);
	            return rv_service.create(reply);
	        } else {
	            throw new Exception("User or Rating not found.");
	        }
	    }
	
	@GetMapping("/reply/feedback/{feedbackId}")
	public List<ReviewReply> getFeedbackId(@PathVariable Integer feedbackId) {
		return rv_service.findByFeedbackId(feedbackId);
}
	

	
	
//	@GetMapping("/feedback/{feedbackId}")
//	public ReviewReply getOne(@PathVariable Integer feedbackId) {
//		return rv_service.findbyId(feedbackId);
//	}
	
//	@PostMapping("/reply/create")
//	public ReviewReply create(@RequestBody ReviewReply reviewReply) {
//		return rv_service.create(reviewReply);
//	}
	
//	 @DeleteMapping("/reply/delete/{replyId}")
//		public void delete(@PathVariable("replyId")ReviewReply id) {
//			rv_service.delete(id);
//	}
	 
	
}
	

