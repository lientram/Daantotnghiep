package com.ps20652.DATN.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.CustomerFeedbackDAO;
import com.ps20652.DATN.dao.OrderDAO;
import com.ps20652.DATN.dao.ProductDAO;
import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.OrderDetail;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.service.OrderService;
import com.ps20652.DATN.service.ProductService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private CustomerFeedbackDAO feedbackDAO;

	@Override
	public Order create(Order order) {
		
		return orderDAO.save(order);
	}

	@Override
	public List<Order> getOrders(String username) {
		return orderDAO.findByUser_Username(username);
	}

	@Override
	public Order getOrderById(Integer orderId) {
		
		return orderDAO.findById(orderId).get();
	}

	@Override
	public List<Order> getAllOrders() {

		return orderDAO.findAll();
	}

	@Override
	public Order updateOrder(Order order) {

		return orderDAO.save(order);
	}

	@Override
	public void cancelOrder(Integer orderId) {
	
		        // Lấy thông tin chi tiết đơn hàng, bao gồm sản phẩm và số lượng, từ cơ sở dữ liệu
		        Order order = orderDAO.findByOrderId(orderId); // Giả sử bạn có OrderRepository để truy vấn đơn hàng từ cơ sở dữ liệu

		        List<OrderDetail> orderDetails = order.getOrderDetails(); // Giả sử mỗi đơn hàng có một danh sách các order details

		        // Cập nhật số lượng hàng còn trong kho
		        for (OrderDetail orderDetail : orderDetails) {
		            Product product = orderDetail.getProduct();
		            int quantityToRestore = orderDetail.getQuantity(); // Số lượng sản phẩm sẽ được tăng lại
		            int currentStock = product.getQuantityInStock(); // Số lượng sản phẩm hiện tại trong kho

		            // Tăng số lượng sản phẩm trong kho
		            product.setQuantityInStock(currentStock + quantityToRestore);
		            productDAO.save(product); // Lưu cập nhật vào cơ sở dữ liệu
		        }

		        // Sau đó, bạn có thể đánh dấu đơn hàng đã bị hủy (tùy thuộc vào logic của ứng dụng của bạn)
		        order.setStatus("Đơn hàng hủy");
		        orderDAO.save(order); // Lưu cập nhật trạng thái đơn hàng vào cơ sở dữ liệu
		    
	

	}

	@Override
	public List<Order> getOrdersByStatus(String status) {
		
		return orderDAO.findByStatus(status);
	}

	 @Override
	    public void deleteById(Integer orderId) {
	        orderDAO.deleteById(orderId);
	    }

	 @Override
	    public Page<Order> getAllOrdersPaginated(PageRequest pageRequest) {
	
	        return orderDAO.findAll(pageRequest);
	    }

	 @Override
	    public Page<Order> getOrdersByUsernamePaginated(String username, Pageable pageable) {
	        // Sử dụng OrderRepository để lấy danh sách đơn hàng của người dùng với phân trang
	        return orderDAO.findByUser_Username(username, pageable);
	    }
}
