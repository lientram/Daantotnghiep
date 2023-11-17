	package com.ps20652.DATN.service.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.AccountDAO;
import com.ps20652.DATN.dao.OrderDAO;
import com.ps20652.DATN.dao.OrderDetailDAO;
import com.ps20652.DATN.dao.ProductDAO;
import com.ps20652.DATN.dao.UserCartDAO;
import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.entity.UserCart;
import com.ps20652.DATN.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	 @Autowired
	    private OrderDAO orderRepository;
	 @Autowired
	    private OrderDetailDAO orderDetailRepository;
	 @Autowired
	    private ProductDAO productRepository;
	 @Autowired
	    private AccountDAO accountRepository;	 
	 @Autowired
	    private UserCartDAO userCartRepository;


	 
	 @Override
	 public UserCart add(Integer userId, Integer productId) {
	     // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng của người dùng chưa
	     UserCart userCart = userCartRepository.findByAccount_UserIdAndProduct_ProductId(userId, productId);

	     if (userCart == null) {
	         // Sản phẩm chưa tồn tại trong giỏ hàng, thêm mới
	         Product product = productRepository.findById(productId).orElse(null);

	         if (product != null) {
	             UserCart cartItem = new UserCart();
	             Account user = accountRepository.findById(userId).orElse(null);
 
	             if (user != null) {
	                 cartItem.setAccount(user);
	                 cartItem.setProduct(product);
	                 cartItem.setQuantity(1); // Đặt quantity (thường là 1 khi thêm sản phẩm mới vào giỏ hàng)
	                 userCartRepository.save(cartItem); // Lưu giỏ hàng mới vào cơ sở dữ liệu
	                 return cartItem;
	             }
	         }
	     } else {
	         // Sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng
	         userCart.setQuantity(userCart.getQuantity() + 1);
	         userCartRepository.save(userCart); // Lưu giỏ hàng cũ sau khi tăng số lượng
	         return userCart;
	     }

	     return null; // Trường hợp sản phẩm không tồn tại hoặc có lỗi khác
	 }



	 @Override
	 public void remove(Integer userId, Integer productId) {
	     UserCart userCart = userCartRepository.findByAccount_UserIdAndProduct_ProductId(userId, productId);

	     if (userCart != null) {
	         userCartRepository.delete(userCart);
	     }
	 }


	 @Override
	 public Product update(Integer userId, Integer productId, int qty) {
	     UserCart userCart = userCartRepository.findByAccount_UserIdAndProduct_ProductId(userId, productId);
	     if (userCart != null) {
	         if (qty > 0) {
	             userCart.setQuantity(qty);
	             userCartRepository.save(userCart);
	             return userCart.getProduct();
	         } else {
	             // Nếu số lượng (qty) là 0 hoặc âm, bạn có thể xóa sản phẩm khỏi giỏ hàng.
	             userCartRepository.delete(userCart);
	             return null;
	         }
	     }
	     return null; // Trường hợp sản phẩm không tồn tại trong giỏ hàng
	 }




	 @Override
	 public int getCount(Integer userId) {
	     List<UserCart> shoppingCart = userCartRepository.findByAccountUserId(userId);
	     return shoppingCart.size();
	 }


	 
	  @Override
	    public int getAmount(Integer userId) {
	        return userCartRepository.sumAmountByAccountUserId(userId);
	    }


		@Override
		public List<UserCart> findById(Integer id) {
			return userCartRepository.findByAccountUserId(id);
		}



		@Override
		public List<UserCart> findByAccountUserId(Integer userId) {
			
			return userCartRepository.findByAccountUserId(userId);
		}



		@Override
		public void clearUserCart(Integer userId) {
			List<UserCart> userCart = userCartRepository.findByAccountUserId(userId);
			for (UserCart cartItem : userCart) {
				userCartRepository.delete(cartItem);
			}
		}



//		@Override
//		public void clear(Integer userId) {
//			
//			    List<UserCart> userCart = userCartRepository.findByAccountUserId(userId);
//			    userCartRepository.deleteAll(userCart);
//			
//
//			
//		}



//		@Override
//		public UserCart findByAccount_UserId(Integer userId) {
//			
//			return userCartRepository.findByAccount_UserId(userId);
//		}

	   
} 


