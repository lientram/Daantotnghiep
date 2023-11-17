package com.ps20652.DATN.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.entity.ReviewReply;
import com.ps20652.DATN.entity.UserCart;

public interface ShoppingCartService {
	
//	List<OrderDetail> cartItems = new ArrayList<>();
	
//	public List<OrderDetail> getCartItems(int userId);
//	
//	 public boolean addToCart(int productId);
//	 public boolean removeFromCart(int orderDetailId);
//	 public boolean checkout();
	
	UserCart add(Integer userId, Integer productId);
	
	List<UserCart> findByAccountUserId(Integer userId);
	
//	Product add(Integer id);
	
	public List<UserCart> findById(Integer id);
	
	void remove(Integer userId, Integer productId);
	
//	void clear(Integer userId);

	Product update(Integer userId, Integer productId, int qty);
	
//	UserCart findByAccount_UserId(Integer userId);
	
//	void clear(Integer cart);

//	Collection<Product> getItems();

	int getCount(Integer userId);

	int getAmount(Integer userId);

	public void clearUserCart(Integer userId);

}