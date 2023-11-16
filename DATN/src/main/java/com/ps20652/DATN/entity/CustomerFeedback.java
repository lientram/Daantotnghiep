package com.ps20652.DATN.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "Customerfeedback")
@Data
public class CustomerFeedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer feedbackId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account customer;

    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "feedback_date")
    private Date feedback_date;

    @Column(name = "content", length = 100)
    private String content;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "customerFeedback")
    private List<ReviewReply> replies;
}
