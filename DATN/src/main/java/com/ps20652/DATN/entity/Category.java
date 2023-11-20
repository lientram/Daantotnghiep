package com.ps20652.DATN.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
@SuppressWarnings("serial")
@Entity
@Table(name = "Categories")
@Data
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "name", length = 100)
    private String name;
    
    @JsonIgnore
    @OneToMany(mappedBy = "category") // A category can have multiple products
    private List<Product> products;
    
    
}
