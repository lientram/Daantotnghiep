package com.ps20652.DATN.service;

import javax.servlet.http.HttpServletRequest;

public interface CookieService {
    String getCartFromCookie(HttpServletRequest request);
}
