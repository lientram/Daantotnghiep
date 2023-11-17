package com.ps20652.DATN.service.impl;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


import org.springframework.stereotype.Service;


import com.ps20652.DATN.service.CookieService;


@Service
public class CookieServiceImpl implements CookieService {

   

     @Override
    public String getCartFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cartValue = "";

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    cartValue = cookie.getValue();
                    break;
                }
            }
        }

        return cartValue;
    }
}



