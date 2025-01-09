package com.example.twspring2.controller;

import com.example.twspring2.database.model.UserEntity;
import com.example.twspring2.database.repository.UserRepository;
import com.example.twspring2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {
    UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String open(Model model) {
        model.addAttribute("user", new UserEntity());
        return "authentication/register";
    }

    @PostMapping()
    public String register(@ModelAttribute("user")UserEntity user) {
        userService.save(user);
        return "redirect:/home";
    }
}
