package com.ps20652.DATN.controller.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps20652.DATN.dao.ProductDAO;
import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.entity.UserCart;
import com.ps20652.DATN.service.AccountService;
import com.ps20652.DATN.service.OrderDetailService;
import com.ps20652.DATN.service.OrderService;
import com.ps20652.DATN.service.ProductService;
import com.ps20652.DATN.service.ShoppingCartService;


//	@RestController
//	@RequestMapping("/api/cart")
//	public class ShoppingCartREST {
//
//	    @Autowired
//	    private ShoppingCartService cartService;
//
//	    @GetMapping
//	    public ResponseEntity<List<OrderDetail>> viewCart() {
//	        List<OrderDetail> cartItems = cartService.getCartItems(1); // Sử dụng CartService để lấy danh sách sản phẩm trong giỏ hàng
//	        return ResponseEntity.ok(cartItems);
//	    }
//
//	    @PostMapping("/add")
//	    public ResponseEntity<String> addToCart(@RequestParam("productId") int productId) {
//	        boolean added = cartService.addToCart(productId); // Sử dụng CartService để thêm sản phẩm vào giỏ hàng
//	        if (added) {
//	            return ResponseEntity.ok("Product added to cart");
//	        } else {
//	            return ResponseEntity.badRequest().body("Product not added to cart");
//	        }
//	    }
//
//	    @DeleteMapping("/{orderDetailId}")
//	    public ResponseEntity<String> removeFromCart(@PathVariable("orderDetailId") int orderDetailId) {
//	        boolean removed = cartService.removeFromCart(orderDetailId); // Sử dụng CartService để xoá sản phẩm khỏi giỏ hàng
//	        if (removed) {
//	            return ResponseEntity.ok("Product removed from cart");
//	        } else {
//	            return ResponseEntity.badRequest().body("Product not removed from cart");
//	        }
//	    }
//
//	    @PostMapping("/checkout")
//	    public ResponseEntity<String> checkout() {
//	        boolean checkoutSuccessful = cartService.checkout(); // Sử dụng CartService để đặt hàng và tạo đơn hàng mới
//	        if (checkoutSuccessful) {
//	            return ResponseEntity.ok("Checkout successful");
//	        } else {
//	            return ResponseEntity.badRequest().body("Checkout failed");
//	        }
//	    }
//	}

@RestController
@RequestMapping("/cart")
public class ShoppingCartREST {

    private final Map<Integer, Product> cart = new HashMap<>();

    @Autowired
    private ProductDAO productRepository;
    @Autowired
    private ShoppingCartService userCartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OrderDetailService orderDetialService;
    
    @PostMapping("/add")
    public UserCart addItemToCart(@RequestParam int userId, @RequestParam int productId) {
        return userCartService.add(userId, productId);
    }
    
    @GetMapping("/get")
    public List<UserCart> getUserCart(@RequestParam int userId) {
        return userCartService.findById(userId);
    }
    
    @DeleteMapping("/remove-product")
    public ResponseEntity<String> removeProductFromCart(
        @RequestParam Integer userId,
        @RequestParam Integer productId
    ) {
        userCartService.remove(userId, productId);
        return ResponseEntity.ok("Product removed from user's cart.");
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateCartItemQuantity(
        @RequestParam Integer userId,
        @RequestParam Integer productId,
        @RequestParam int quantity) {

        Product updatedProduct = userCartService.update(userId, productId, quantity);

        if (updatedProduct != null) {
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Sản phẩm không tồn tại trong giỏ hàng hoặc có lỗi khác.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public int getCartItemCount(@RequestParam Integer userId) {
        int itemCount = userCartService.getCount(userId);
        return itemCount;
    }
    
    @GetMapping("/total-amount")
    public ResponseEntity<Integer> getTotalAmount(@RequestParam("userId") Integer userId) {
        try {
            int totalAmount = userCartService.getAmount(userId);
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @PostMapping("/checkout")
    public ResponseEntity<String> processCheckout(@RequestParam Integer userId) {
        try {
            List<UserCart> userCart = userCartService.findByAccountUserId(userId);

            if (userCart.isEmpty()) {
                return ResponseEntity.badRequest().body("Giỏ hàng của bạn trống.");
            }
            Account user = accountService.findbyId(userId);
            
            

            // Tạo đơn hàng mới
            Order order = new Order();
            order.setUser(user); // Đặt ID của người dùng
            order.setOrderDate(new Date()); // Đặt ngày đặt hàng
            order.setStatus("Pending"); // Đặt trạng thái đơn hàng

            // Tính tổng tiền dựa trên thông tin các sản phẩm trong giỏ hàng
            double totalAmount = 0;
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (UserCart cartItem : userCart) {
                Product product = cartItem.getProduct();
                totalAmount += product.getPrice() * cartItem.getQuantity();
                
                // Tạo đơn hàng chi tiết và thêm vào danh sách đơn hàng chi tiết
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setPrice(product.getPrice());
                orderDetails.add(orderDetail);
            }

            order.setTotalPrice(totalAmount);
            
            // Lưu đơn hàng và đơn hàng chi tiết vào cơ sở dữ liệu
            orderService.create(order);
            orderDetialService.create(orderDetails);

            

            return ResponseEntity.ok("Đặt hàng thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi trong quá trình đặt hàng.");
        }
    }
}





