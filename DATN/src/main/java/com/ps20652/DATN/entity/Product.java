package com.ps20652.DATN.entity;

import lombok.Data;

import javax.persistence.*;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
@SuppressWarnings("serial")
@Entity
@Table(name = "Products")
@Data
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "price")
    private Integer price;
    

    @Column(name = "quantity_in_stock")
    private int quantityInStock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    
    private Category category;

   
    @Column(name = "image")
    private String image;
    
    @Column(name = "quantity_added")
    private Integer quantityAdded;
    
    @Column(name = "purchase_price")
    private Integer purchasePrice;
    

//     @Override
// public String toString() {
//     return "Product{" +
//             "productId=" + productId +
//             ", name='" + name + '\'' +
//             ", image='" + image + '\'' +
//             ", price=" + price +
//             ", quantityInStock=" + quantityInStock +
//             ", category=" + category +
//             '}';
// }


}


