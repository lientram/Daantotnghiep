package com.ps20652.DATN.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Voucher;
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.ShoppingCartService;
import com.ps20652.DATN.service.VoucherService;

@Controller
@RequestMapping("vouchers")
public class UserVoucherController {
	@Autowired
    VoucherService voucherService;
	@Autowired
    AccountService accountService;
	@Autowired
    private ShoppingCartService cartService;
	  @GetMapping
	    public String adminVoucher(Model model, Principal principal,  @RequestParam(name = "confirmationMessage", required = false) String confirmationMessage) {
		  
		  if (principal != null) {
	            String username = principal.getName();
	            Account user = accountService.findByUsername(username);
	            List<Voucher> vouchers = voucherService.findAll();
	            
	            int userId = getUserIDByUsername(username);
	            int cartItemCount = cartService.getCount(userId);
	            model.addAttribute("cartItemCount", cartItemCount);
	            
	            
	 		   model.addAttribute("vouchers", vouchers);
	            model.addAttribute("user", user);
	            model.addAttribute("username", username);
	            
	            
	            
	            if (confirmationMessage != null) {
	                model.addAttribute("confirmationMessage", confirmationMessage);
	            }
	            return "user/vouchers";
	        }

	        return "redirect:/login";
	      
	    }
	  
	  @PostMapping("/add-voucher")
	  public String addVoucherToUser(@RequestParam String password, @RequestParam Integer userId, @RequestParam Integer voucherId, Model model,  RedirectAttributes redirectAttributes) {
	      // Tìm người dùng thông qua ID
	      Account user = accountService.findbyId(userId);
	      
	      if (user != null) {
	          // Tìm voucher thông qua ID
	          Voucher voucher = voucherService.findbyId(voucherId);
	          
	          if (voucher != null) {
	              // Thêm voucher vào tài khoản người dùng
	              user.getVouchers().add(voucher);
	            
	              accountService.update(user);
	              redirectAttributes.addFlashAttribute("confirmationMessage", "Đã lấy voucher thành công");
	          } else {
	              model.addAttribute("message", "Thêm Voucher vào kho thất bại");
	          }
	      } else {
	          model.addAttribute("message", "Không tìm thấy người dùng");
	      }

	  
	      return "redirect:/vouchers";
	  }
	  
	  @GetMapping("/voucher-stock")
	    public String getUserVouchers(Model model, Principal principal) {
	        if (principal != null) {
	            String username = principal.getName();
	            model.addAttribute("username", username);
	            Account user = accountService.findByUsername(username);
	            int userId = getUserIDByUsername(username);
	            int cartItemCount = cartService.getCount(userId);
	            model.addAttribute("cartItemCount", cartItemCount);

	            if (user != null) {
	                model.addAttribute("user", user);
	                return "user2/profile/voucher"; // Tên trang HTML bạn đã tạo
	            }
	        }

	        return "redirect:/login"; // Điều hướng về trang đăng nhập nếu không có người dùng đăng nhập
	    }
	  private int getUserIDByUsername(String username) {
	        // Sử dụng Spring Data JPA để truy vấn cơ sở dữ liệu
	        Account user = accountService.findByUsername(username);

	        if (user != null) {
	            return user.getUserId(); // Trả về userID từ đối tượng User
	        }

	        return -1; // Trường hợp không tìm thấy user
	    }

}


	




