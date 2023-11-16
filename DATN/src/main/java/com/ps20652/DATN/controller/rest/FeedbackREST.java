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
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.FeedbackService;
import com.ps20652.DATN.service.ProductService;

@RestController
public class FeedbackREST {

	@Autowired 
	private FeedbackService fb_service;
	
	@Autowired 
	private AccountService a_service;
	
	@Autowired
	private ProductService p_service;
	
	
	 @PostMapping("/feedback/create")
	    public CustomerFeedback createRating(@RequestBody CustomerFeedback customerFeedback) throws Exception {
	        // Tìm người dùng và sản phẩm liên quan
	        Account account = a_service.findbyIdAcc(customerFeedback.getCustomer());
	        Product product = p_service.findbyIdPro(customerFeedback.getProduct());

	        if (account != null && product != null) {
	        	customerFeedback.setCustomer(account);
	        	customerFeedback.setProduct(product);
	            return fb_service.create(customerFeedback);
	        } else {
	            throw new Exception("User or Product not found.");
	        }	
	    }
	 
	 @GetMapping("/feedback/product/{productId}")
		public List<CustomerFeedback> getProductId(@PathVariable Integer productId) {
			return fb_service.findByProductProductId(productId);
	}
	
	@GetMapping("/getFeedback")
	public List<CustomerFeedback> getFeedback() {
		return fb_service.findAll();
	}
	@GetMapping("/feedback/{feedbackId}")
	public CustomerFeedback getOne(@PathVariable Integer feedbackId) {
		return fb_service.findbyId(feedbackId);
	}
	
	 @GetMapping("/feedback/userId/{userId}")
		public List<CustomerFeedback> getUserId(@PathVariable Integer userId) {
			return fb_service.findByUserId(userId);
	}
	 @DeleteMapping("/feedback/delete/{feedbackId}")
		public void delete(@PathVariable("feedbackId")CustomerFeedback id) {
			fb_service.delete(id);
	}
	 
}
	

