package com.travelandrepeat.api.service;

import com.travelandrepeat.api.entity.User;

public interface UserService {
    User getUserByEmail(String email);
}
