package com.ps20652.DATN.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.Product;


public interface ProductService {

	
	
	public List<Product> findAll();

	public Product create(Product pro);

	public Product findbyId(Integer id);
	
	public Product findbyIdPro(Product product);
	
	Product update(Product product);

	void delete(Product id);
	
	List<Product> findByName(String name);
	
	List<Product> findByPrice(double minPrice, double maxPrice);
	
	List<Product> findByCategoryCategoryId(int categoryId);
	
	public List<Product> getTop4BestSellingProductsPerCategory(int categoryId);
	
	 Page<Product> getAllOrdersPaginated(PageRequest pageRequest);
	 
	 void updateQuantityInStock(Integer productId, int quantityAdded);
	 
	 
}