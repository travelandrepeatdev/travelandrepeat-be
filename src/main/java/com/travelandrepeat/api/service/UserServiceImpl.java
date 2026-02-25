package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.User;
import com.travelandrepeat.api.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
