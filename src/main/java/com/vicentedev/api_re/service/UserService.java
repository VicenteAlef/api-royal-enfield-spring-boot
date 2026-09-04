package com.vicentedev.api_re.service;

import com.vicentedev.api_re.dto.request.UpdateUserRoleRequest;
import com.vicentedev.api_re.dto.response.UserResponse;
import com.vicentedev.api_re.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    Page<UserResponse> list(Boolean neverAccessed, Role role, String query, Pageable pageable);

    UserResponse getById(UUID id);

    UserResponse updateRole(UUID id, UpdateUserRoleRequest request);

    void delete(UUID id);
}
