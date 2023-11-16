package com.ps20652.DATN.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.CategoryDAO;
import com.ps20652.DATN.entity.Category;
import com.ps20652.DATN.service.CategoryService;

@Service
public class CategoryServiceImp implements CategoryService {

	@Autowired
	CategoryDAO categoryDAO;
	
	
	@Override
	public List<Category> findAll() {
		return categoryDAO.findAll();
	}


	@Override
	public Category findbyId(Integer categoryId) {
		return categoryDAO.findById(categoryId).get();
	}


	@Override
	public Category create(Category cat) {
		return categoryDAO.save(cat);
	}
	
	@Override
	public Category update(Category cat) {
		return categoryDAO.save(cat);
	}


	@Override
	public void delete(Category id) {
		categoryDAO.delete(id);
	}

}
