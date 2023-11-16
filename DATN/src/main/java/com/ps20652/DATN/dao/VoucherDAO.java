package com.ps20652.DATN.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ps20652.DATN.entity.Voucher;

@Repository
public interface VoucherDAO extends JpaRepository<Voucher, Integer> {
   
}