//package com.ps20652.DATN.controller.rest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.ps20652.DATN.entity.Order;
//import com.ps20652.DATN.service.OrderService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/order")
//public class OrderREST {
//
//    @Autowired
//    private OrderService orderService;
//
//    @GetMapping("/getOrder")
//    public ResponseEntity<List<Order>> getAllOrders() {
//        List<Order> orders = orderService.getAllOrders();
//        return ResponseEntity.ok(orders);
//    }
//
//    @GetMapping("/{orderId}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
//        Order order = orderService.getOrderById(orderId);
//        if (order != null) {
//            return ResponseEntity.ok(order);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    
//
//}
//
