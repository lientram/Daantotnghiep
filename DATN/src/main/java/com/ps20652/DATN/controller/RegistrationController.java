package com.ps20652.DATN.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ps20652.DATN.entity.Account;
import com.ps20652.DATN.service.AccountService;

@Controller
@RequestMapping("/")
public class RegistrationController {

    @Autowired
    private AccountService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Account());
        return "security2/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Account user, Model model) {
        if (userService.isUserExists(user.getUsername())) {
            model.addAttribute("registrationError", "Username đã tồn tại");
            return "security2/register";
        }
        userService.create(user);
        return "redirect:/login";
    }
}
