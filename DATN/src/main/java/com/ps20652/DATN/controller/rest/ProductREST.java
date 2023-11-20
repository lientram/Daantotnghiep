package com.ps20652.DATN.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Category;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductREST {

	@Autowired 
	ProductService p_service;
	
	@GetMapping("/product")
	public List<Product> getProduct() {
		return p_service.findAll();
	}
	@PostMapping("/product/create")
	public Product createe(@RequestBody Product pro) {
		return p_service.create(pro);
	}
	@GetMapping("/product/{id}")
	public Product getOne(@PathVariable Integer id) {
		return p_service.findbyId(id);
	}
	@PutMapping("/product/{id}")
	public Product update(@RequestBody Product product,@PathVariable("id") Integer id) {
		return p_service.update(product);
	}
	@DeleteMapping("/product/delete/{id}")
	public void delete(@PathVariable("id")Product id) {
		p_service.delete(id);
	}
	  @GetMapping("/products/searchByName")
	    public List<Product> searchProductsByName(@RequestParam String name) {
	        return p_service.findByName(name);
    }
	    @GetMapping("/product/searchByPrice")
	    public List<Product> searchProductsByPrice(
	        @RequestParam("minPrice") double minPrice,
	        @RequestParam("maxPrice") double maxPrice) {
	        return p_service.findByPrice(minPrice, maxPrice);
    }
	    @GetMapping("/products/category/{categoryId}")
		public List<Product> getcategoryId(@PathVariable Integer categoryId) {
			return p_service.findByCategoryCategoryId(categoryId);
	}
	  
}