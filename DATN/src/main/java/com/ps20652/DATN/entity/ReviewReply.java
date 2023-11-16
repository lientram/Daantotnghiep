package com.ps20652.DATN.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Table(name = "Reviewreply")
@Data
public class ReviewReply implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Integer replyId;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private CustomerFeedback customerFeedback;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account customer;

    @Column(name = "reply_text", length = 255)
    private String replyText;
    

}
