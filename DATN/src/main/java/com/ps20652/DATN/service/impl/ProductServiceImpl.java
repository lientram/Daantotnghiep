package com.ps20652.DATN.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ps20652.DATN.dao.ProductDAO;
import com.ps20652.DATN.dao.StockHistoryDAO;
import com.ps20652.DATN.entity.Category;
import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.entity.StockHistory;
import com.ps20652.DATN.service.CategoryService;
import com.ps20652.DATN.service.OrderDetailService;
import com.ps20652.DATN.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private CategoryService catService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private StockHistoryDAO stockHistoryDAO;
	
	@Override
	public List<Product> findAll() {
		return productDAO.findAll();
	}

	@Override
	public Product create(Product product) {
		return productDAO.save(product);
	}

	@Override
	public Product findbyId(Integer id) {
		return productDAO.findById(id).get();
	}

	@Override
	public void delete(Product id) {
		productDAO.delete(id);
		
	}

	@Override
	public List<Product> findByName(String name) {
		return productDAO.findByName(name);
	}

	@Override
	public List<Product> findByPrice(double minPrice, double maxPrice) {
		
		return productDAO.findByPrice(minPrice, maxPrice);
	}

	@Override
	public Product update(Product product) {
		return productDAO.save(product);
	}

	@Override
	public List<Product> findByCategoryCategoryId(int categoryId) {
	
		return productDAO.findByCategoryCategoryId(categoryId);
	}

	@Override
	public Product findbyIdPro(Product product) {
		return productDAO.findById(product.getProductId()).get();
	}

		
	

//	// Tính tổng số lượng đã bán của một sản phẩm
	private int getTotalUnitsSold(Product product) {
	    List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByProduct(product);
	    int totalUnitsSold = orderDetails.stream().mapToInt(OrderDetail::getQuantity).sum();
//	    System.out.println("Total units sold for Product ID " + product.getProductId() + ": " + totalUnitsSold);
	    return totalUnitsSold;
	}

	@Override
	public List<Product> getTop4BestSellingProductsPerCategory(int categoryId) {
	    List<Product> top4ProductsPerCategory = new ArrayList<>();
	    Category category = catService.findbyId(categoryId); // Sử dụng service để tìm danh mục bằng ID

	    if (category != null) {
	        List<Product> productsInCategory = productDAO.findByCategory(category);

	        // Sắp xếp sản phẩm trong danh mục theo số lượng đã bán
	        productsInCategory.sort((p1, p2) -> {
	            int unitsSold1 = getTotalUnitsSold(p1);
	            int unitsSold2 = getTotalUnitsSold(p2);
	            return Integer.compare(unitsSold2, unitsSold1);
	        });
	        
//	        productsInCategory.forEach(product -> {
//	            System.out.println("Product ID: " + product.getProductId() + product.getCategory().getName() + ", Units Sold: " + getTotalUnitsSold(product));
//	        });

	        // Lấy top 4 sản phẩm của danh mục và thêm vào danh sách chứa top 4 sản phẩm
	        top4ProductsPerCategory = productsInCategory.stream().limit(4).collect(Collectors.toList());
	    }

	    return top4ProductsPerCategory;
	}

	@Override
	public Page<Product> getAllOrdersPaginated(PageRequest pageRequest) {
	
		return productDAO.findAll(pageRequest);
	}

	 @Override
	    @Transactional
	    public void updateQuantityInStock(Integer productId, int quantityAdded) {
	        // Cập nhật số lượng trong kho
	        Product product = productDAO.findById(productId).orElse(null);
	        if (product != null) {
	
	            productDAO.updateQuantityInStock(productId, quantityAdded);
	            productDAO.save(product);
	            
	            // Thêm lịch sử nhập hàng
	            StockHistory stockHistory = new StockHistory();
	            stockHistory.setProduct(product);
	            stockHistory.setQuantityAdded(quantityAdded);
	            stockHistory.setEntryDate(new Date());
	            stockHistoryDAO.save(stockHistory);
	        }
	    }





}
