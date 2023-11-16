package com.ps20652.DATN.DTO;

import org.springframework.web.multipart.MultipartFile;

import com.ps20652.DATN.entity.Category;

public class ProductDTO {
    private int productId;
    private String name;
    private String description;
    private Integer price;
    private double priceStaff;
    private int quantityInStock;
    private MultipartFile image;
    private Category category;
    private Integer quantityAdded;
    private Integer purchasePrice;

    // Getter methods
    public int getProductId() {
        return productId;
    }
    public Integer getQuantityAdded() {
        return quantityAdded;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }
    
    public double getPriceStaff() {
        return priceStaff;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

   
    
    public MultipartFile getImage() {
        return image;
    }

    public Category getCategory() {
        return category;
    }

    // Setter methods
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setQuantityAdded(Integer quantityAdded) {
        this.quantityAdded = quantityAdded;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    public void setPriceStaff(double priceStaff) {
        this.priceStaff = priceStaff;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    
    

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}

