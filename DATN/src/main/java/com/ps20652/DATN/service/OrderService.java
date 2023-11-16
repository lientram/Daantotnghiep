package com.ps20652.DATN.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ps20652.DATN.entity.Order;



public interface OrderService {

	
	


	public Order create(Order order);
	
	List<Order> getOrders (String username);
	
	Order getOrderById (Integer orderId);
	
	List<Order> getAllOrders();	
	
	Order updateOrder(Order order);

	
	 public void cancelOrder(Integer orderId);
	
	 public List<Order> getOrdersByStatus(String status);
	 
	 void deleteById(Integer orderId);
	 
	 Page<Order> getAllOrdersPaginated(PageRequest pageRequest);
	 
	 public Page<Order> getOrdersByUsernamePaginated(String username, Pageable pageable);
	 
//	public Product findbyId(Integer id);
//	
//	public Product findbyIdPro(Product product);
//	
//	Product update(Product product);
//
//	void delete(Product id);
//	
//	List<Product> findByName(String name);
//	
//	List<Product> findByPrice(double minPrice, double maxPrice);
//	
//	List<Product> findByCategoryCategoryId(int categoryId);
}