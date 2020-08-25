package com.example.forum.controller;

import com.example.forum.domain.User;
import com.example.forum.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class Oauth2EntryController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/login/googleAuth")
    public String googleAuth(){
        return "oauth2";
    }

    @GetMapping("/login/success")
    public String getUserInfoOauth2(OAuth2AuthenticationToken authenticationToken) {
        OAuth2User auth2User = authenticationToken.getPrincipal();
        Map<String, Object> userAttributes = auth2User.getAttributes();
        if (userAttributes != null) {
            String username = (String) userAttributes.get("email");
            username = username.substring(0, username.indexOf("@"));
            if(userRepo.findByUsername(username) == null){
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setEmail((String) userAttributes.get("email"));
                newUser.setGender((String) userAttributes.get("gender"));
                newUser.setActive(true);
                newUser.setOauth2Name((String) userAttributes.get("sub"));
                userRepo.save(newUser);
            }
        }
        return "redirect:/";
    }

}
