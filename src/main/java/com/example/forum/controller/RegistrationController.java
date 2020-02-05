package com.example.forum.controller;

import com.example.forum.domain.Role;
import com.example.forum.domain.User;
import com.example.forum.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userfromdb = userRepo.findByUsername(user.getUsername());
        if(userfromdb != null) {
            model.put("message", "User already exists");
            return "registration";
        }
        if(user.getUsername().equals("") | user.getPassword().equals("")) {
            model.put("message", "Field(s) cannot be empty");
            return "registration";
        }
        else {
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            userRepo.save(user);
            return "redirect:/login";
        }
    }
}
