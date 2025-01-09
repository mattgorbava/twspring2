package com.example.twspring2.controller;

import com.example.twspring2.database.repository.UserRepository;
import com.example.twspring2.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/home")
public class HomeController {
    UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String open(Model model, Principal principal) {
        if (principal instanceof AuthenticatedUser authenticatedUser) {
            model.addAttribute("username", authenticatedUser.getUsername());
        } else {
            model.addAttribute("username", principal.getName());
        }
        return "user/home";
    }
}
