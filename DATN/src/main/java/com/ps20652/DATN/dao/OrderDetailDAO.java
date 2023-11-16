package com.ps20652.DATN.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;

@Repository
public interface OrderDetailDAO extends JpaRepository<OrderDetail, Integer> {

	 OrderDetail findByProduct_ProductId(int productId);
	
	 List<OrderDetail> findByOrder_User_UserId(int userId);
	 
	 List<OrderDetail> findByOrder_OrderId(int orderId);
	 
	 List<OrderDetail> findByProduct( Product product);
}
