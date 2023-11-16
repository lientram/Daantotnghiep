package com.ps20652.DATN.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ps20652.DATN.dao.OrderDAO;
import com.ps20652.DATN.entity.CustomerFeedback;
import com.ps20652.DATN.entity.Order;
import com.ps20652.DATN.service.OrderService;
import com.ps20652.DATN.service.RevenueService;

@Controller
public class AdminOrderController {
    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderService orderService;
    @Autowired
    private RevenueService revenueService;

    @GetMapping("/admin/orders")
    public String getAllOrders(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sort", defaultValue = "orderDate,desc") String sort,
            @RequestParam(name = "confirmationMessage", required = false) String confirmationMessage,
            Model model) {
        int pageSize = 6; // Số lượng đơn hàng trên mỗi trang

        String[] sortProperties = sort.split(",");
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.fromString(sortProperties[1]), sortProperties[0]);

        Page<Order> orderPage = orderService.getAllOrdersPaginated(PageRequest.of(page, pageSize, Sort.by(sortOrder)));

        model.addAttribute("orders", orderPage);

        // Thêm thông báo vào model nếu có
        if (confirmationMessage != null) {
            model.addAttribute("confirmationMessage", confirmationMessage);
        }

        return "AdminCpanel/ui-typography";
    }

    @PostMapping("/admin/orders/confirm")
    public String confirmOrder(@RequestParam Integer orderId, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrderById(orderId);

        if (order != null) {
            order.setStatus("Đang giao hàng"); // Thay đổi trạng thái đơn hàng
            orderService.updateOrder(order);

            // Thêm thông báo vào redirectAttributes
            redirectAttributes.addFlashAttribute("confirmationMessage", "Xác nhận đơn hàng thành công");
        }

        return "redirect:/admin/orders";
    }

    @PostMapping("/admin/orders/cancel")
    public String cancelOrder(@RequestParam Integer orderId, RedirectAttributes redirectAttributes) {

        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            order.setStatus("Chờ xác nhận"); // Thay đổi trạng thái đơn hàng
            orderService.updateOrder(order);

            redirectAttributes.addFlashAttribute("confirmationMessage", "Hủy xác nhận thành công");
        }
        return "redirect:/admin/orders";

    }

    @PostMapping("/admin/orders/moveToTrash")
    public String moveToTrash(@RequestParam("orderId") Integer orderId, RedirectAttributes redirectAttributes) {
        // Viết logic để thay đổi trạng thái của đơn hàng sang "Thùng rác"
        // Ví dụ, sử dụng một service để cập nhật trạng thái của đơn hàng

        Order order = orderDAO.findByOrderId(orderId); // Lấy đơn hàng từ ID

        if (order != null) {
            // Thay đổi trạng thái thành "Thùng rác" hoặc một trạng thái tương ứng
            order.setStatus("Thùng rác");

            // Lưu đơn hàng đã cập nhật trạng thái
            orderService.updateOrder(order);

            redirectAttributes.addFlashAttribute("confirmationMessage", "Chuyển đơn hàng vào thùng rác thành công");
        }

        // Chuyển hướng về trang danh sách đơn hàng sau khi thực hiện chuyển vào thùng
        // rác
        return "redirect:/admin/orders"; // Thay đổi đường dẫn tùy theo cấu trúc routing của bạn
    }

    @GetMapping("/admin/orders/trash")
    public String viewTrashOrders(Model model) {
        List<Order> trashOrders = orderService.getOrdersByStatus("Thùng rác"); // Lấy đơn hàng có trạng thái là Thùng
                                                                               // rác

        model.addAttribute("trashOrders", trashOrders);
        return "AdminCpanel/ui-typography-trash"; // Trả về template Thymeleaf để hiển thị đơn hàng trong thùng rác
    }

    @PostMapping("/admin/orders/restoreFromTrash")
    public String restoreOrderFromTrash(@RequestParam("orderId") Integer orderId, Model model) {
        Order orderOptional = orderDAO.findByOrderId(orderId);

        if (orderOptional != null) {
            orderOptional.setStatus("Đơn hàng hủy");

            // Lưu đơn hàng đã cập nhật trạng thái
            orderService.updateOrder(orderOptional);
        }

        return "redirect:/admin/orders/trash"; // Điều hướng về trang danh sách đơn hàng
    }

    @PostMapping("/admin/orders/deletePermanently")
    public String deleteOrderPermanently(@RequestParam("orderId") Integer orderId, Model model) {
        Order orderOptional = orderDAO.findByOrderId(orderId);

        if (orderOptional != null) {

            revenueService.deleteByOrderId(orderId);
            orderService.deleteById(orderId); // Xóa đơn hàng khỏi cơ sở dữ liệu
        }

        return "redirect:/admin/orders/trash"; // Điều hướng về trang danh sách đơn hàng
    }

}
