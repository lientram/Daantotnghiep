package com.ps20652.DATN.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.OrderDAO;
import com.ps20652.DATN.dao.RevenueDAO;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.entity.Product;
import com.ps20652.DATN.entity.Revenue;
import com.ps20652.DATN.entity.StockHistory;
import com.ps20652.DATN.service.RevenueService;


@Service
public class RevenueServiceImpl implements RevenueService {

	 @Autowired
	 private OrderDAO orderRepository;
	 @Autowired
	 private RevenueDAO revenueRepository;
	
	 public List<Revenue> calculateRevenueForYear(int year) {
		 
	        List<Revenue> revenues = new ArrayList<>();
	        // Tính toán doanh thu cho mỗi tháng trong năm
	        for (int month = 1; month <= 12; month++) {
	            Date startDate = getStartDate(year, month);
	            Date endDate = getEndDate(year, month);

	            List<Order> ordersForMonth = orderRepository.findByOrderDateBetween(startDate, endDate);

	            Double totalRevenueForMonth = calculateTotalRevenue(ordersForMonth);

	            // Tạo đối tượng Revenue và thêm vào danh sách revenues
	            Revenue revenue = new Revenue();
	            revenue.setOrderDate(startDate); // Đặt ngày bắt đầu của tháng
	            revenue.setTotalAmount(totalRevenueForMonth);
	            revenues.add(revenue);
	        }

	        return revenues;
	    }

	    // Tính tổng doanh thu từ danh sách các đơn hàng
	 private Double calculateTotalRevenue(List<Order> orders) {
		    Double total = 0.0;
		    for (Order order : orders) {
		        total += order.getTotalPrice();
		    }
		    return total;
		}
//	 private Double calculateTotalPurchaseCost(List<StockHistory> histories) {
//		    Double total = 0.0;
//		    for (StockHistory stockHistory : histories) {
//		        total += stockHistory.getProduct().getPurchasePrice() * stockHistory.getQuantityAdded();
//		    }
//		    return total;
//		}

	    // Hàm hỗ trợ để lấy ngày bắt đầu của tháng
	    private Date getStartDate(int year, int month) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(year, month - 1, 1); // Đặt ngày đầu tiên của tháng
	        return calendar.getTime();
	    }

	    // Hàm hỗ trợ để lấy ngày kết thúc của tháng
	    private Date getEndDate(int year, int month) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(year, month - 1, 1); // Đặt ngày đầu tiên của tháng
	        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	        calendar.set(Calendar.DAY_OF_MONTH, lastDay); // Đặt ngày cuối cùng của tháng
	        return calendar.getTime();
	    }

		@Override
	    public List<Revenue> getAllRevenues() {
	        return revenueRepository.findAll();

	}

		@Override
		public Revenue create(Revenue revenue) {
		
			return revenueRepository.save(revenue);
		}

		@Override
		 @Transactional
		public void deleteByOrderId(Integer orderId) {
			revenueRepository.deleteByOrder_OrderId(orderId);
			
		}
}
