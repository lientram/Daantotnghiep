package com.ps20652.DATN.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String extractMonthFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM"); // Định dạng để lấy tháng
        return dateFormat.format(date); // Trả về chuỗi tháng
    }
}