package com.ps20652.DATN.controller;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.entity.ReviewReply;
import com.ps20652.DATN.entity.Voucher;
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.FeedbackService;
import com.ps20652.DATN.service.ProductService;
import com.ps20652.DATN.service.ReviewReplyService;

@Controller
@RequestMapping("/admin")
public class AdminFeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private ReviewReplyService replyService;
    @Autowired
    private AccountService accountService;
//    @Autowired
//    private ReviewReplyService replyService;
  
//    @GetMapping("/list")
//    public String listFeedbacks(Model model) {
//        List<CustomerFeedback> feedbacks = feedbackService.findAll();
//        model.addAttribute("feedbacks", feedbacks);
//        return "feedback/list";
//    }

    @GetMapping("/feedback")
    public String showAllFeedback(Model model,  Principal principal) {
    	
    	  if (principal != null) {
              String username = principal.getName();
              int id = getUserIDByUsername(username);
              model.addAttribute("adminId", id);
              
          }
    	
    	List<CustomerFeedback> feedbacks = feedbackService.findAll();
    	
    	model.addAttribute("feedbacks", feedbacks);
        
        return "AdminCpanel/feedback";
    }
    
    @PostMapping("/feedback")
    public String replyFeedback(Model model, 
    		@RequestParam("replyText") String replyText,
    		@RequestParam("feedbackId") Integer feedbackId,
    		@RequestParam("adminId") Integer adminId
    		) {
    	
    	  
    
    	
    	Account acc = accountService.findbyId(adminId);	
    	CustomerFeedback feedback = feedbackService.findbyId(feedbackId);
    	
    	feedback.setStatus("Đã trả lời");
    	feedbackService.create(feedback);
    	
    	
    	  ReviewReply reply = new ReviewReply();
    	  reply.setReplyText(replyText);
    	  reply.setCustomer(acc);
    	  reply.setCustomerFeedback(feedback);
    	
    	replyService.create(reply);
    	
    	
        
        return "redirect:/admin/feedback";
    }
    private int getUserIDByUsername(String username) {
        // Sử dụng Spring Data JPA để truy vấn cơ sở dữ liệu
        Account user = accountService.findByUsername(username);

        if (user != null) {
            return user.getUserId(); // Trả về userID từ đối tượng User
        }

        return -1; // Trường hợp không tìm thấy user
    }

//    @PostMapping("/create")
//    public String createFeedback(@ModelAttribute CustomerFeedback customerFeedback) {
//        feedbackService.create(customerFeedback);
//        return "redirect:/";
//    }
//    
  
    
 
    
   
    
}