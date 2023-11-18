package com.ps20652.DATN.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ps20652.DATN.entity.Voucher;
import com.ps20652.DATN.service.VoucherService;

@Controller
@RequestMapping("/admin/vouchers") // Định tuyến các yêu cầu liên quan đến vouchers
public class AdminVoucherController {
	
	@Autowired
    VoucherService voucherService;

	  @GetMapping
	    public String adminVoucher(Model model, @RequestParam(name = "confirmationMessage", required = false) String confirmationMessage) {

		  List<Voucher> vouchers = voucherService.findAll();
		  
		   model.addAttribute("vouchers", vouchers);
		   
		   if (confirmationMessage != null) {
		         model.addAttribute("confirmationMessage", confirmationMessage);
		     }
		  
	        return "AdminCpanel/vouchers";
	    }

	  @PostMapping
	  public String addVoucher(@RequestParam("voucherCode") String voucherCode, 
	                           @RequestParam("discountAmount") int discountAmount,
	                           @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	                           @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
	                           @RequestParam("quantity") int quantity,
	                           RedirectAttributes redirectAttributes) {
	      // Xử lý việc tạo đối tượng Voucher từ các giá trị được truyền vào
	      Voucher voucher = new Voucher();
	      voucher.setVoucherCode(voucherCode);
	      voucher.setDiscountAmount(discountAmount);
	      voucher.setStartDate(startDate);
	      voucher.setEndDate(endDate);
	      voucher.setQuantity(quantity);
	      
	      voucherService.createVoucher(voucher);
	      redirectAttributes.addFlashAttribute("confirmationMessage", "Tạo mã giảm giá thành công");
	      return "redirect:/admin/vouchers"; // Chuyển hướng sau khi tạo voucher thành công
	  }
	  
	  @PostMapping("/delete")
	    public String deleteVoucher(@RequestParam Integer voucherId, RedirectAttributes redirectAttributes) {
	        // Gọi phương thức xóa mã giảm từ service
	        voucherService.deleteVoucher(voucherId);
	        redirectAttributes.addFlashAttribute("confirmationMessage", "Xóa mã giảm giá thành công");
	        // Redirect về trang danh sách mã giảm với thông báo
	        return "redirect:/admin/vouchers";
	    }

}





