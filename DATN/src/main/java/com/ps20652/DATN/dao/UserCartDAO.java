package com.ps20652.DATN.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ps20652.DATN.entity.ReviewReply;
import com.ps20652.DATN.entity.UserCart;

public interface UserCartDAO  extends JpaRepository<UserCart, Integer> {

	UserCart findByAccount_UserIdAndProduct_ProductId(Integer userId, Integer productId);
//	
	List<UserCart> findByAccountUserId(Integer userId);
	
//	UserCart findByAccount_UserId(Integer userId);
	
	@Query("SELECT SUM(u.product.price * u.quantity) FROM UserCart u WHERE u.account.userId = :userId")
    int sumAmountByAccountUserId(@Param("userId") Integer userId);
//	
//	UserCart findByProductId(Integer productId);
	
	
}
