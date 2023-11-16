package com.ps20652.DATN.entity;

import java.io.Serializable;

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
@Data
@Table(name = "user_cart")
public class UserCart implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id") // Tạo mối quan hệ với bảng User thông qua cột user_id
	private Account account;

	@ManyToOne
	@JoinColumn(name = "product_id") // Tạo mối quan hệ với bảng Product thông qua cột product_id
	private Product product;

	@Column(name = "quantity")
	private Integer quantity;

}
