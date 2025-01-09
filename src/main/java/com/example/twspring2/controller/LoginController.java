package com.example.twspring2.controller;

import com.example.twspring2.database.model.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String open(Model model) {
        model.addAttribute("user",new UserEntity());
        return "authentication/login";
    }

    @PostMapping
    public void post(Model model) {
        //empty
    }
}
