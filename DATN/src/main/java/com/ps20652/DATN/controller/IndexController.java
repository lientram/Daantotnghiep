package com.ps20652.DATN.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Category;
import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;

import com.ps20652.DATN.entity.ReviewReply;
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.CategoryService;
import com.ps20652.DATN.service.FeedbackService;
import com.ps20652.DATN.service.OrderDetailService;
import com.ps20652.DATN.service.ProductService;
import com.ps20652.DATN.service.ReviewReplyService;
import com.ps20652.DATN.service.ShoppingCartService;
import com.ps20652.DATN.service.UploadService;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderDetailService orderDetailService;
    // @Autowired
    // private ReviewReplyService replyService;
    //

    @GetMapping
    public String listProducts(Model model, Authentication authentication,
            @RequestParam(name = "confirmationMessage", required = false) String confirmationMessage) {
        List<CustomerFeedback> feedback = feedbackService.findAll();
        List<Product> products = productService.findAll();
        List<Category> cat = categoryService.findAll();
        Map<Integer, Integer> unitsSoldMap = new HashMap<>();
        Map<Integer, List<Product>> topProductsPerCategory = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);

            // Lấy userID của người dùng đã đăng nhập
            int userId = getUserIDByUsername(username);

            // Lấy số lượng sản phẩm trong giỏ hàng
            int cartItemCount = cartService.getCount(userId);
            model.addAttribute("cartItemCount", cartItemCount);
        }

        for (Product product : products) {
            int unitsSold = orderDetailService.getProductSell(product);
            unitsSoldMap.put(product.getProductId(), unitsSold);
        }

        // for (Category category : cat) {
        // List<Product> topProducts =
        // productService.getTop4BestSellingProductsPerCategory(category.getCategoryId());
        // model.addAttribute("topProductsPerCategory_" + category.getCategoryId(),
        // topProducts);
        // }

        for (Category category : cat) {
            List<Product> topProducts = productService.getTop4BestSellingProductsPerCategory(category.getCategoryId());
            topProductsPerCategory.put((category.getCategoryId()), topProducts);
        }

        if (confirmationMessage != null) {
            model.addAttribute("confirmationMessage", confirmationMessage);
        }

        model.addAttribute("topProductsPerCategory", topProductsPerCategory);
        model.addAttribute("unitsSoldMap", unitsSoldMap);
        model.addAttribute("products", products);
        model.addAttribute("categories", cat);
        model.addAttribute("fb", feedback);

        return "user2/index2";
    }

    @GetMapping("/login")
    public String login() {
        return "security2/login2";
    }

    @GetMapping("/new")
    public String New() {
        return "main/New";
    }

    @GetMapping("/contact")
    public String contact() {
        return "main/Contact";
    }

    @GetMapping("/about")
    public String About() {
        return "main/About";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "redirect:/"; // Trang thông báo lỗi truy cập không được phép
    }

    @GetMapping("/ProductDetails/{productId}")
    public String IDProducts(@PathVariable("productId") Integer productId, Model model, Principal principal) {
        Product product = productService.findbyId(productId);
        List<CustomerFeedback> feedback = feedbackService.findByProductProductId(productId);

        if (principal != null) {
            String username = principal.getName();
            int userId = getUserIDByUsername(username);
            model.addAttribute("userId", userId);
        }

        model.addAttribute("products", product);
        model.addAttribute("feedbacks", feedback);

        return "user2/product-details";
    }

    // @GetMapping ("/ProductDetails/{productId}")
    // public String IDProducts(@PathVariable("productId") Integer productId,Model
    // model, @RequestParam Integer feedbackId ) {
    // Product product = productService.findbyId(productId);
    // List <CustomerFeedback> feedback =
    // feedbackService.findByProductProductId(productId);
    // List <ReviewReply> rep = replyService.findByFeedbackId(feedbackId);
    // model.addAttribute("products", product);
    // model.addAttribute("reply", rep);
    // model.addAttribute("feedbacks", feedback);
    // return "product-details";
    // }

    @GetMapping("/searchName")
    public String searchProductsNAME(@RequestParam("name") String productName, Model model) {
        // Sử dụng phương thức tìm kiếm theo tên từ ProductDAO
        // Đây là ví dụ tìm kiếm theo tên sản phẩm
        List<Product> searchResults = productService.findByName(productName);

        model.addAttribute("products", searchResults);

        return "user2/index2"; // Trả về view để hiển thị kết quả tìm kiếm
    }

    @GetMapping("/searchPrice")
    public String price(HttpServletRequest request, Model model, @RequestParam("min") double min,
            @RequestParam("max") double max) {
        // List<Product> items = dao.findByPriceBetween(min, max);

        List<Product> searchResults = productService.findByPrice(min, max);
        model.addAttribute("products", searchResults);

        return "user2/index2"; // Trả về view để hiển thị kết quả lọc
    }

    @GetMapping("/product/category/{categoryId}")
    public String productCategory(@PathVariable("categoryId") Integer categoryId, Model model) {
        List<Category> allcat = categoryService.findAll();
        Category cat = categoryService.findbyId(categoryId);
        List<Product> products = productService.findByCategoryCategoryId(categoryId);
        model.addAttribute("categories", cat);
        model.addAttribute("products", products);
        model.addAttribute("allcategory", allcat);
        return "user2/product-category";
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
