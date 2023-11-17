package com.ps20652.DATN.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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



	
	@Override
    public void deleteExpiredVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        Date currentDate = new Date();

        for (Voucher voucher : vouchers) {
            Date endDate = voucher.getEndDate();
            if (endDate != null && endDate.before(currentDate)) {
                voucherRepository.delete(voucher);
            }
        }
    }

	@Scheduled(cron = "0 0 0 * * ?") // Chạy vào mỗi ngày lúc 00:00:00
    public void scheduleTaskToDeleteExpiredVouchers() {
        deleteExpiredVouchers(); // Gọi phương thức xóa voucher hết hạn
    }

}