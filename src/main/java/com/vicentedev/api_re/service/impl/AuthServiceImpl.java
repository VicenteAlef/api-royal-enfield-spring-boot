package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.dto.request.LoginRequest;
import com.vicentedev.api_re.dto.request.RegisterRequest;
import com.vicentedev.api_re.dto.request.Verify2FaRequest;
import com.vicentedev.api_re.dto.response.AuthResponse;
import com.vicentedev.api_re.dto.response.LoginStepResponse;
import com.vicentedev.api_re.dto.response.UserResponse;
import com.vicentedev.api_re.entity.OtpCode;
import com.vicentedev.api_re.entity.Role;
import com.vicentedev.api_re.entity.User;
import com.vicentedev.api_re.exception.BusinessException;
import com.vicentedev.api_re.exception.ResourceNotFoundException;
import com.vicentedev.api_re.mapper.UserMapper;
import com.vicentedev.api_re.repository.OtpCodeRepository;
import com.vicentedev.api_re.repository.UserRepository;
import com.vicentedev.api_re.service.AuthService;
import com.vicentedev.api_re.service.EmailService;
import com.vicentedev.api_re.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpCodeRepository otpCodeRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${app.otp.expiration-minutes}")
    private int otpExpirationMinutes;

    public AuthServiceImpl(
            UserRepository userRepository,
            OtpCodeRepository otpCodeRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.otpCodeRepository = otpCodeRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessException("Email is already registered: " + normalizedEmail);
        }

        User user = User.builder()
                .name(request.name().trim())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_VISITOR)
                .build();

        User saved = userRepository.save(user);

        emailService.sendNewUserRegisteredAlert(saved.getName(), saved.getEmail(), saved.getRole());

        return userMapper.toResponse(saved);
    }

    @Override
    public LoginStepResponse initiateLogin(LoginRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }

        otpCodeRepository.deleteByUserId(user.getId());

        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));

        OtpCode otpCode = OtpCode.builder()
                .user(user)
                .code(otp)
                .expiresAt(OffsetDateTime.now().plusMinutes(otpExpirationMinutes))
                .used(false)
                .build();

        otpCodeRepository.save(otpCode);

        emailService.send2FaCode(user.getEmail(), user.getName(), otp);

        return new LoginStepResponse(true, user.getEmail(), "Authentication code sent to your email");
    }

    @Override
    public AuthResponse verify2Fa(Verify2FaRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new BusinessException("User not found with email: " + normalizedEmail));

        OtpCode otpCode = otpCodeRepository.findByUserIdAndCodeAndUsedFalse(user.getId(), request.code().trim())
                .orElseThrow(() -> new BusinessException("Invalid or expired authentication code"));

        if (otpCode.getExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new BusinessException("Authentication code has expired. Please initiate login again");
        }

        otpCode.setUsed(true);
        otpCodeRepository.save(otpCode);

        user.setLastLoginAt(OffsetDateTime.now());
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationTimeMs(),
                userMapper.toResponse(savedUser)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(User principal) {
        if (principal == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }
        return userMapper.toResponse(principal);
    }
}
