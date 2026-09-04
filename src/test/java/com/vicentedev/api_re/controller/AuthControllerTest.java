package com.vicentedev.api_re.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import com.vicentedev.api_re.dto.request.LoginRequest;
import com.vicentedev.api_re.dto.request.RegisterRequest;
import com.vicentedev.api_re.dto.request.Verify2FaRequest;
import com.vicentedev.api_re.entity.OtpCode;
import com.vicentedev.api_re.entity.Role;
import com.vicentedev.api_re.entity.User;
import com.vicentedev.api_re.repository.OtpCodeRepository;
import com.vicentedev.api_re.repository.UserRepository;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

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
    void shouldRegisterNewUserWithVisitorRole() throws Exception {
        RegisterRequest request = new RegisterRequest("Recrutador Tech", "recrutador@empresa.com", "senha123");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("Recrutador Tech")))
                .andExpect(jsonPath("$.email", is("recrutador@empresa.com")))
                .andExpect(jsonPath("$.role", is(Role.ROLE_VISITOR.name())))
                .andExpect(jsonPath("$.lastLoginAt", nullValue()));
    }

    @Test
    void shouldFailWhenRegisteringDuplicateEmail() throws Exception {
        RegisterRequest request = new RegisterRequest("Usuario Um", "duplicado@empresa.com", "senha123");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Business Rule Violation")));
    }

    @Test
    void shouldInitiateLoginAndSendOtp() throws Exception {
        RegisterRequest regReq = new RegisterRequest("Mariana Silva", "mariana@empresa.com", "senha123");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated());

        LoginRequest loginReq = new LoginRequest("mariana@empresa.com", "senha123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requires2FA", is(true)))
                .andExpect(jsonPath("$.email", is("mariana@empresa.com")));
    }

    @Test
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        LoginRequest loginReq = new LoginRequest("naoexiste@empresa.com", "senhaErrada");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Business Rule Violation")));
    }

    @Test
    void shouldVerifyOtpAndReturnJwtTokenAndSetLastLogin() throws Exception {
        RegisterRequest regReq = new RegisterRequest("Carlos Alberto", "carlos@empresa.com", "senhaSegura123");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated());

        LoginRequest loginReq = new LoginRequest("carlos@empresa.com", "senhaSegura123");
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk());

        User user = userRepository.findByEmailIgnoreCase("carlos@empresa.com").orElseThrow();
        OtpCode otpCode = otpCodeRepository.findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(user.getId()).orElseThrow();

        Verify2FaRequest verifyReq = new Verify2FaRequest("carlos@empresa.com", otpCode.getCode());

        String authResponse = mockMvc.perform(post("/api/v1/auth/verify-2fa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.tokenType", is("Bearer")))
                .andExpect(jsonPath("$.user.email", is("carlos@empresa.com")))
                .andExpect(jsonPath("$.user.lastLoginAt", notNullValue()))
                .andReturn().getResponse().getContentAsString();

        String jwtToken = objectMapper.readTree(authResponse).get("token").asText();

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("carlos@empresa.com")))
                .andExpect(jsonPath("$.role", is("ROLE_VISITOR")));
    }

    @Test
    void shouldFailVerifyOtpWithInvalidCode() throws Exception {
        RegisterRequest regReq = new RegisterRequest("Roberto", "roberto@empresa.com", "senhaSegura123");
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated());

        LoginRequest loginReq = new LoginRequest("roberto@empresa.com", "senhaSegura123");
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk());

        Verify2FaRequest invalidVerifyReq = new Verify2FaRequest("roberto@empresa.com", "999999");

        mockMvc.perform(post("/api/v1/auth/verify-2fa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVerifyReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Business Rule Violation")));
    }
}
