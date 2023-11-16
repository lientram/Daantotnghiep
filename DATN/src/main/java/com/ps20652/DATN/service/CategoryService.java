package com.ps20652.DATN.service;

import java.util.List;

import com.ps20652.DATN.entity.Category;

public interface CategoryService {

	public List<Category> findAll();
	
	public Category findbyId(Integer categoryId);
	
	public Category create(Category cat);
	
	Category update(Category id);
	
	void delete(Category id);
	
}
