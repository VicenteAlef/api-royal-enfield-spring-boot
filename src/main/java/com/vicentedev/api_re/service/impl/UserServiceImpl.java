package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.dto.request.UpdateUserRoleRequest;
import com.vicentedev.api_re.dto.response.UserResponse;
import com.vicentedev.api_re.entity.Role;
import com.vicentedev.api_re.entity.User;
import com.vicentedev.api_re.exception.ResourceNotFoundException;
import com.vicentedev.api_re.mapper.UserMapper;
import com.vicentedev.api_re.repository.UserRepository;
import com.vicentedev.api_re.repository.specification.UserSpecification;
import com.vicentedev.api_re.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> list(Boolean neverAccessed, Role role, String query, Pageable pageable) {
        Specification<User> spec = UserSpecification.withFilters(neverAccessed, role, query);
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateRole(UUID id, UpdateUserRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setRole(request.role());
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        userRepository.delete(user);
    }
}
