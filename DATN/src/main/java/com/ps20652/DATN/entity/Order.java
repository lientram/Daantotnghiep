package com.ps20652.DATN.entity;

import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@SuppressWarnings("serial")
@Entity
@Table(name = "Orders")
@Data
public class Order implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "status", length = 50)
    private String status;
    
    @Column(name = "phone")
    private Integer phone;
    
    @Column(name = "address", length = 255)
    private String address;
    
    @Column(name = "fullname", length = 255)
    private String fullName;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher; // Thêm trường để lưu trữ thông tin về mã voucher đã sử dụng

}

