package com.ps20652.DATN.dao;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ps20652.DATN.entity.Category;
import com.ps20652.DATN.entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
	 List<Product> findByName(String name);
	 
	 
	 @Query("SELECT o FROM Product o WHERE o.price BETWEEN ?1 AND ?2")
   	List<Product> findByPrice(double minPrice, double maxPrice);
	 
    List<Product> findByCategoryCategoryId(int categoryId);
    
    List<Product> findByCategory(Category category);
    
    @Modifying
    @Query("UPDATE Product p SET p.quantityInStock = p.quantityInStock + :quantityAdded WHERE p.productId = :productId")
    void updateQuantityInStock(@Param("productId") Integer productId, @Param("quantityAdded") int quantityAdded);
    
}
