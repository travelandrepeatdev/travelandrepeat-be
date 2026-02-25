package com.travelandrepeat.api.service;

import com.travelandrepeat.api.dto.LoginRequest;
import com.travelandrepeat.api.dto.LoginResponse;
import com.travelandrepeat.api.dto.UserLoginDetails;

public interface LoginService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse profile(UserLoginDetails userLoginDetails);
}
