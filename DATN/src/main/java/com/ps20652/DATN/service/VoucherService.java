package com.ps20652.DATN.service;

import java.util.List;

import com.ps20652.DATN.entity.Voucher;

public interface VoucherService {
    Voucher createVoucher(Voucher voucher);
    
    public List<Voucher> findAll();
 
    public Voucher findbyId(Integer id);
    
    public void deleteVoucher(Integer voucherId);
    
    public void deleteExpiredVouchers();
}
