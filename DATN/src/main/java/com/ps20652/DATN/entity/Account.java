package com.ps20652.DATN.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;



@SuppressWarnings("serial")

@Entity
@Table(name = "Account")
@Data
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username", length = 50)
    private String username;
    
    @Column(name = "phonenumber")
    private Integer phonenumber;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "role", length = 50)
    private String role = "USER";
    
    @Column(name = "photo")
    private String photo;
    
    @Column(name = "otp")
    private String otp;
    
    @Column(name = "otp_created_at")
    private LocalDateTime otpCreatedAt;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Account_Voucher",	
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "voucher_id")
    )

      @JsonIgnore
    private Set<Voucher> vouchers = new HashSet<>();
    
    public boolean hasVoucher(Voucher voucher) {
        return vouchers.contains(voucher);
    }
}
