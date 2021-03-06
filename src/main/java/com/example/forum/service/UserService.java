package com.example.forum.service;

import com.example.forum.domain.User;
import com.example.forum.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFindByUsername = userRepo.findByUsername(username);
        User userFindByOauth2Name = userRepo.findByOauth2Name(username);
        if(userFindByOauth2Name != null)
            return userFindByOauth2Name;
        else
            return userFindByUsername;
    }
}
