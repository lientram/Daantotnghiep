package com.ps20652.DATN.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.entity.Category;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.service.CategoryService;

@RestController
public class CategoryREST {

	@Autowired 
	private CategoryService c_service;
	
	
	@GetMapping("/getCategory")
	public List<Category> getCategory() {
		return c_service.findAll();
	}
	@PostMapping("/category/create")
	public Category createCategory(@RequestBody Category cat) {
		return c_service.create(cat);
	}
	@PutMapping("/category/{categoryId}")
	public Category update(@RequestBody Category cat,@PathVariable("categoryId") Integer id) {
		return c_service.update(cat);
	}
}
