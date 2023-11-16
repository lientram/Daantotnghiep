package com.ps20652.DATN.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ps20652.DATN.DTO.AccountDTO;
import com.ps20652.DATN.DTO.ProductDTO;
import com.ps20652.DATN.dao.AccountDAO;
import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.ShoppingCartService;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private AccountDAO userService;
    @Autowired
	private PasswordEncoder passwordEncoder;
    @Autowired
	private ResourceLoader resourceLoader;
    @Autowired
	private AccountService accountService;
    
    @Autowired
	private ShoppingCartService cartService;
    
    @GetMapping
    public String userProfile(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Account user = userService.findByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("username", username);
            
            int userId = getUserIDByUsername(username);
            int cartItemCount = cartService.getCount(userId);
            model.addAttribute("cartItemCount", cartItemCount);
        }
        return "user2/profile/info";
    }
   
 

    	 @RequestMapping("/editProfile")
        public String editProfile(Model model, Principal principal) {
            if (principal != null) {
                String username = principal.getName();
                model.addAttribute("username", username);
                // Load thông tin người dùng để hiển thị trong trang chỉnh sửa thông tin
                Account user = userService.findByUsername(username);
                model.addAttribute("user", user);
            }
            return "user/editProfile"; // Trả về trang chỉnh sửa thông tin
        }
    	 
    	 @RequestMapping("/updateProfile")
    	    public String updateProfile(@RequestParam Integer userId,
    	    							@RequestParam Integer phonenumber,
    	                                @RequestParam String fullName,
    	                                @RequestParam String address,  
    	                                @ModelAttribute("accountDTO") AccountDTO editedAccount,
    	                                @RequestParam("photo") MultipartFile photo,
    	                                Principal principal) {

    	        // Kiểm tra xem người dùng đã đăng nhập chưa
    	        if (principal != null) {
    	            String username = principal.getName();
    	            
    	            
    	            // Lấy thông tin người dùng từ cơ sở dữ liệu
    	            Account user = userService.findByUsername(username);
    	            
    	            
    	            String img = user.getPhoto();
    	            String imageString = img;
    	            
    	            if (!photo.isEmpty()) {
		    	        try {
		    	        
		    	        	  // Lấy đường dẫn thực của dự án
		                    Resource resource = resourceLoader.getResource("classpath:/");
		                    String projectPath = resource.getFile().getAbsolutePath();

		                    // Đường dẫn lưu trữ hình ảnh
		                    String uploadPath = projectPath + "/static/assets/images/";
		                    Path path = Paths.get(uploadPath);
		    			
		    	            // Lưu trữ hình ảnh vào thư mục uploads
		    	            Files.copy(photo.getInputStream(), path.resolve(photo.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
		    	            
		    	            // Lấy tên hình ảnh đã lưu
		    	            imageString = photo.getOriginalFilename();
		    	        } catch (IOException e) {	
		    	            e.printStackTrace();
		    	            // Xử lý lỗi khi tải lên hình ảnh
		    	         
		    	        }
		    	    }
    	            
    	            
    	            
    	            // Cập nhật thông tin người dùng nếu người dùng tồn tại
    	            if (user != null) {
    	                user.setPhonenumber(editedAccount.getPhonenumber());
    	                user.setFullName(editedAccount.getFullName());
    	                user.setAddress(editedAccount.getAddress());
    	                
        	            user.setPhoto(imageString);
    	                
    	             
    	                
    	                
    	                // Lưu thông tin người dùng cập nhật vào cơ sở dữ liệu
    	                userService.save(user);
    	            }
    	        }
    	        
    	        // Sau khi cập nhật thông tin, chuyển hướng người dùng về trang profile hoặc trang chính
    	        return "redirect:/profile";
    	    }
    	 
    	 @GetMapping("/editPassword")
         public String editPassword(Model model, Principal principal) {
             if (principal != null) {
                 String username = principal.getName();
                 model.addAttribute("username", username);
                 // Load thông tin người dùng để hiển thị trong trang chỉnh sửa thông tin
                 Account user = userService.findByUsername(username);
                 model.addAttribute("user", user);
             }
             return "user/changePass"; // Trả về trang chỉnh sửa thông tin
         }
    	 

    	 
    	 @PostMapping("/editPassword")
    	    public String updatePassword(@RequestParam("userId") Integer userId,
    	                                 @RequestParam("currentPassword") String currentPassword,
    	                                 @RequestParam("newPassword") String newPassword,
    	                                 @RequestParam("confirmNewPassword") String confirmNewPassword,
    	                                 Model model, Principal principal) {

    	        // Assuming UserService or UserRepository methods to fetch user details
    	        Account user = accountService.findbyId(userId);

    	        if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
    	            if (newPassword.equals(confirmNewPassword)) {
    	                // Encode the new password before saving
    	                String encodedNewPassword = passwordEncoder.encode(newPassword);
    	                user.setPassword(encodedNewPassword);

    	                // Save the updated user (this is a simplification, please use your data access layer)
    	                accountService.update(user);

    	                // Redirect to profile page or a success page
    	                return "redirect:/profile";
    	            } else {
    	                model.addAttribute("error", "New password and confirmation do not match");
    	            }
    	        } else {
    	            model.addAttribute("error", "Current password is incorrect");
    	        }

    	        if (principal != null) {
                    String username = principal.getName();
                    // Load thông tin người dùng để hiển thị trong trang chỉnh sửa thông tin
                    Account userr = userService.findByUsername(username);
                    model.addAttribute("user", userr);
                }
    	        
    	        return "user/changePass";
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

 
