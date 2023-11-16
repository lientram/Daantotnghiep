package com.ps20652.DATN.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps20652.DATN.dao.VoucherDAO;
import com.ps20652.DATN.entity.Voucher;
import com.ps20652.DATN.service.VoucherService;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    VoucherDAO voucherRepository;

    

    @Override
    public Voucher createVoucher(Voucher voucher) {
 
        return voucherRepository.save(voucher);
    }



	@Override
	public List<Voucher> findAll() {
	
		return voucherRepository.findAll();
	}



	@Override
	public Voucher findbyId(Integer id) {
		return voucherRepository.findById(id).get();
	}



	@Override
	
	public void deleteVoucher(Integer voucherId) {
		voucherRepository.deleteById(voucherId);
		
	}
}