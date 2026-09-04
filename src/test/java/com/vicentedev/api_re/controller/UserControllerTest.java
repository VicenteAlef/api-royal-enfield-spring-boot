package com.vicentedev.api_re.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import com.vicentedev.api_re.dto.request.UpdateUserRoleRequest;
import com.vicentedev.api_re.entity.Role;
import com.vicentedev.api_re.entity.User;
import com.vicentedev.api_re.repository.OtpCodeRepository;
import com.vicentedev.api_re.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpCodeRepository otpCodeRepository;

    @BeforeEach
    void setUp() {
        otpCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldDenyAccessToUsersEndpointForAnonymous() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "visitor@empresa.com", roles = {"VISITOR"})
    void shouldDenyAccessToUsersEndpointForVisitor() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "operador@empresa.com", roles = {"USER"})
    void shouldDenyAccessToUsersEndpointForRegularUser() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin@royalenfield.com.br", roles = {"ADMIN"})
    void shouldAllowAdminToListUsers() throws Exception {
        User user1 = User.builder()
                .name("Recrutador 1")
                .email("rec1@empresa.com")
                .password("hash123")
                .role(Role.ROLE_VISITOR)
                .build();
        User user2 = User.builder()
                .name("Operador 1")
                .email("op1@empresa.com")
                .password("hash123")
                .role(Role.ROLE_USER)
                .lastLoginAt(OffsetDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin@royalenfield.com.br", roles = {"ADMIN"})
    void shouldFilterUsersWhoNeverAccessed() throws Exception {
        User userNever = User.builder()
                .name("Nunca Acessou")
                .email("nunca@empresa.com")
                .password("hash123")
                .role(Role.ROLE_VISITOR)
                .lastLoginAt(null)
                .build();
        User userAccessed = User.builder()
                .name("Ja Acessou")
                .email("acessou@empresa.com")
                .password("hash123")
                .role(Role.ROLE_VISITOR)
                .lastLoginAt(OffsetDateTime.now())
                .build();

        userRepository.save(userNever);
        userRepository.save(userAccessed);

        mockMvc.perform(get("/api/v1/users").param("neverAccessed", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].email", is("nunca@empresa.com")));
    }

    @Test
    @WithMockUser(username = "admin@royalenfield.com.br", roles = {"ADMIN"})
    void shouldAllowAdminToUpdateUserRole() throws Exception {
        User user = User.builder()
                .name("Recrutador Promovido")
                .email("recrutador@teste.com")
                .password("hash123")
                .role(Role.ROLE_VISITOR)
                .build();
        User saved = userRepository.save(user);

        UpdateUserRoleRequest updateRoleReq = new UpdateUserRoleRequest(Role.ROLE_USER);

        mockMvc.perform(patch("/api/v1/users/" + saved.getId() + "/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRoleReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("ROLE_USER")));
    }

    @Test
    @WithMockUser(username = "admin@royalenfield.com.br", roles = {"ADMIN"})
    void shouldAllowAdminToDeleteUser() throws Exception {
        User user = User.builder()
                .name("Conta Desativada")
                .email("desativada@teste.com")
                .password("hash123")
                .role(Role.ROLE_VISITOR)
                .build();
        User saved = userRepository.save(user);

        mockMvc.perform(delete("/api/v1/users/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/" + saved.getId()))
                .andExpect(status().isNotFound());
    }
}
