package com.ps20652.DATN.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ps20652.DATN.DTO.ProductDTO;
import com.ps20652.DATN.entity.Account;

import com.ps20652.DATN.service.AccountService;

@Controller
@RequestMapping("/admin/accounts")
public class AdminAccountController {

	@Autowired

	private AccountService accountService;

	// @GetMapping
	// public String listAccounts(Model model) {
	// List<Account> account = accountService.findAll();
	// model.addAttribute("accounts", account);
	// return "aaa/ui-alerts";
	// }

	@GetMapping
	public String listAccounts(Model model, Principal principal) {
		// Lấy tài khoản đang đăng nhập
		String loggedInUsername = principal.getName();

		model.addAttribute("username", loggedInUsername);

		// Lấy tất cả các tài khoản từ repository
		List<Account> allAccounts = accountService.findAll();

		// Loại bỏ tài khoản đang đăng nhập khỏi danh sách
		List<Account> otherAccounts = allAccounts.stream()
				.filter(account -> !account.getUsername().equals(loggedInUsername))
				.collect(Collectors.toList());

		// Thêm danh sách các tài khoản khác vào model
		model.addAttribute("accounts", otherAccounts);

		return "AdminCpanel/ui-alerts";
	}

	@PostMapping("/add")
	public String addAccount(@ModelAttribute("account") Account account) {
		// Thực hiện lưu tài khoản vào cơ sở dữ liệu thông qua accountService
		accountService.create(account);
		return "redirect:/admin/accounts"; // Sau khi thêm tài khoản, chuyển hướng đến danh sách tài khoản
	}

	@GetMapping("edit/{userId}")
	public String showEditAccountForm(@PathVariable("userId") Integer userId, Model model) {
		// Lấy sản phẩm từ cơ sở dữ liệu theo productId
		Account account = accountService.findbyId(userId);

		// Kiểm tra xem sản phẩm có tồn tại hay không
		if (account == null) {
			return "redirect:/admin/accounts";
		}
		model.addAttribute("account", account);

		return "AdminCpanel/edit-account";
	}

	@PostMapping("/edit/{userId}")
	public String handleEditProductForm(@ModelAttribute("userId") Account account,
			@ModelAttribute("productDTO") ProductDTO editedProduct, RedirectAttributes redirectAttributes) {
		accountService.update(account);
		return "redirect:/admin/accounts";
	}

	@GetMapping("/delete/{userId}")
	public String deleteAccount(@PathVariable("userId") Integer userId, RedirectAttributes redirectAttributes) {
		try {
			// Kiểm tra xem sản phẩm có tồn tại không
			Account account = accountService.findbyId(userId);
			if (account != null) {
				// Nếu sản phẩm tồn tại, thực hiện xóa sản phẩm
				accountService.delete(account);
				redirectAttributes.addFlashAttribute("successMessage", "Tài khoản đã được xóa thành công.");
			} else {
				// Nếu sản phẩm không tồn tại, hiển thị thông báo lỗi
				redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy Tài khoản để xóa.");
			}
		} catch (Exception e) {
			// Xử lý lỗi nếu có
			redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa Tài Khoản");
		}

		return "redirect:/admin/accounts";
	}

	@GetMapping("/searchUserName")
	public String searchName(@RequestParam("username") String username, Model model, Principal principal) {

		String loggedInUsername = principal.getName();

		model.addAttribute("username", loggedInUsername);

		Account searchResults = accountService.findByUsername(username);

		model.addAttribute("accounts", searchResults);

		return "AdminCpanel/ui-alerts"; // Trả về view để hiển thị kết quả tìm kiếm
	}

	@GetMapping("/searchId")
	public String searchId(@RequestParam("userId") Integer userId, Model model) {
		// Sử dụng phương thức tìm kiếm theo Id từ ProductDAO
		// Đây là ví dụ tìm kiếm theo Id sản phẩm
		Account searchResults = accountService.findbyId(userId);

		model.addAttribute("accounts", searchResults);

		return "AdminCpanel/ui-alerts"; // Trả về view để hiển thị kết quả tìm kiếm
	}
}
