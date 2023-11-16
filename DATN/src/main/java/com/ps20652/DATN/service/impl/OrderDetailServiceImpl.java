package com.ps20652.DATN.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.OrderDAO;
import com.ps20652.DATN.dao.OrderDetailDAO;
import com.ps20652.DATN.dao.ProductDAO;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.service.OrderDetailService;
import com.ps20652.DATN.service.OrderService;
import com.ps20652.DATN.service.ProductService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	private OrderDetailDAO orderDetailDAO;



	 @Override
	    public List<OrderDetail> create(List<OrderDetail> orderDetails) {
	        // Sử dụng repository để lưu danh sách đơn hàng chi tiết vào cơ sở dữ liệu
	        return orderDetailDAO.saveAll(orderDetails);
	    }



	@Override
	public List<OrderDetail> getOrderDeTails(int orderId) {
		
		return orderDetailDAO.findByOrder_OrderId(orderId);
	}






	@Override
	public int getProductSell(Product product) {
		 List<OrderDetail> orderDetails = orderDetailDAO.findByProduct(product);
	        int totalUnitsSold = 0;

	        for (OrderDetail orderDetail : orderDetails) {
	            totalUnitsSold += orderDetail.getQuantity();
	        }

	        return totalUnitsSold;
	    }



	@Override
	 public List<OrderDetail> getOrderDetailsByProduct(Product product) {
        return orderDetailDAO.findByProduct(product);
    }



	@Override
	public OrderDetail getOrderDetailById(Integer orderDetailId) {
	
		return orderDetailDAO.findById(orderDetailId).get();
	}
	
	
	


}
