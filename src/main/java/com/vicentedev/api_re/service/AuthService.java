package com.vicentedev.api_re.service;

import com.vicentedev.api_re.dto.request.LoginRequest;
import com.vicentedev.api_re.dto.request.RegisterRequest;
import com.vicentedev.api_re.dto.request.Verify2FaRequest;
import com.vicentedev.api_re.dto.response.AuthResponse;
import com.vicentedev.api_re.dto.response.LoginStepResponse;
import com.vicentedev.api_re.dto.response.UserResponse;
import com.vicentedev.api_re.entity.User;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    LoginStepResponse initiateLogin(LoginRequest request);

    AuthResponse verify2Fa(Verify2FaRequest request);

    UserResponse getCurrentUser(User principal);
}
