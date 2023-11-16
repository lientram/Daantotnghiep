package com.ps20652.DATN.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ps20652.DATN.dao.ProductDAO;
import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.entity.ReviewReply;
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.FeedbackService;
import com.ps20652.DATN.service.OrderDetailService;
import com.ps20652.DATN.service.OrderService;
import com.ps20652.DATN.service.ProductService;
import com.ps20652.DATN.service.ReviewReplyService;

@Controller
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewReplyService reviewReplyService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

//    @GetMapping("/list")
//    public String listFeedbacks(Model model) {
//        List<CustomerFeedback> feedbacks = feedbackService.findAll();
//        model.addAttribute("feedbacks", feedbacks);
//        return "feedback/list";
//    }

//    @GetMapping("/create")
//    public String createFeedbackForm(Model model) {
//        model.addAttribute("customerFeedback", new CustomerFeedback());
//        return "create";
//    }
//
//    @PostMapping("/create")
//    public String createFeedback(@ModelAttribute CustomerFeedback customerFeedback) {
//        feedbackService.create(customerFeedback);
//        return "redirect:/";
//    }
    
  
    
    @PostMapping("/reply/{productId}")
    public String createReply(@RequestParam(value = "userId", required = false) Integer userId, @PathVariable("productId") Integer productId, @RequestParam("feedbackId") Integer feedbackId, Principal principal, @ModelAttribute ReviewReply reviewReply, Model model) {
        if (principal != null) {
            if (userId != null) {
                Account user = accountService.findbyId(userId);
                reviewReply.setCustomer(user);
                reviewReply.setCustomerFeedback(feedbackService.findbyId(feedbackId));
                
                // Thiết lập các thông tin khác và lưu reply vào cơ sở dữ liệu
                reviewReplyService.create(reviewReply);
                return "redirect:/ProductDetails/" + productId; // Sửa URL redirect để sử dụng ID sản phẩm cụ thể
            } else {
                // Nếu không có userId, có thể thực hiện một hành động khác hoặc trả về trang lỗi
                return "redirect:/login";
            }
        } else {
            return "redirect:/login"; // Nếu chưa đăng nhập, chuyển hướng người dùng đến trang đăng nhập
        }
    }


        
    
    
    @PostMapping("/submitReview")
    public String submitReview(@ModelAttribute CustomerFeedback customerFeedback ,@RequestParam("productId") Integer productId, @RequestParam("userId") Integer userId, @RequestParam("content") String content, @RequestParam("orderId") Integer orderId, @RequestParam("orderDetailId") Integer orderDetailId) {
        // Xử lý việc lưu đánh giá vào cơ sở dữ liệu, ví dụ:
        // reviewService.saveReview(productId, orderId, review);
    	
    	Account acc = accountService.findbyId(userId);
    	Product pro = productService.findbyId(productId);
    	Order order = orderService.getOrderById(orderId);
    	OrderDetail orderDetail = orderDetailService.getOrderDetailById(orderDetailId);
    	customerFeedback.setFeedback_date(new Date());
    	customerFeedback.setCustomer(acc);
    	customerFeedback.setContent(content);
    	customerFeedback.setProduct(pro);
    	customerFeedback.setOrder(order);
    	customerFeedback.setOrderDetail(orderDetail);
    	customerFeedback.setStatus("Đã đánh giá");
 
    	
    	feedbackService.create(customerFeedback);
        // Redirect đến trang cần thiết sau khi gửi đánh giá, ví dụ trang chi tiết đơn hàng
        return "redirect:/orderDetails?orderId=" + orderId;
    }
    
    
    
}