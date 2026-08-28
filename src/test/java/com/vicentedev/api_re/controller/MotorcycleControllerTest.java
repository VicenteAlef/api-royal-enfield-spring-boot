package com.vicentedev.api_re.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import com.vicentedev.api_re.dto.request.MotorcycleCreateRequest;
import com.vicentedev.api_re.dto.request.MotorcycleUpdateRequest;
import com.vicentedev.api_re.dto.request.TechnicalSpecRequest;
import com.vicentedev.api_re.repository.MotorcycleRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MotorcycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    @BeforeEach
    void setUp() {
        motorcycleRepository.deleteAll();
    }

    @Test
    void shouldCreateMotorcycleSuccessfully() throws Exception {
        TechnicalSpecRequest spec = new TechnicalSpecRequest(
                "47 hp", "52.3 Nm", new BigDecimal("241.00"),
                new BigDecimal("15.70"), 740, "6 speed",
                "320mm disc", "300mm disc", "Air-Oil cooled"
        );

        MotorcycleCreateRequest request = new MotorcycleCreateRequest(
                "Super Meteor 650",
                "Cruiser",
                648,
                new BigDecimal("33990.00"),
                "Premium cruiser from Royal Enfield",
                true,
                spec
        );

        mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.modelName", is("Super Meteor 650")))
                .andExpect(jsonPath("$.family", is("Cruiser")))
                .andExpect(jsonPath("$.technicalSpec.powerHp", is("47 hp")))
                .andExpect(jsonPath("$.technicalSpec.seatHeightMm", is(740)));
    }

    @Test
    void shouldReturnValidationErrorsWhenCreatingInvalidMotorcycle() throws Exception {
        MotorcycleCreateRequest invalidRequest = new MotorcycleCreateRequest(
                "",
                "",
                -10,
                new BigDecimal("-50.00"),
                null,
                true,
                null
        );

        mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Validation Error")))
                .andExpect(jsonPath("$.invalidFields.modelName").exists())
                .andExpect(jsonPath("$.invalidFields.family").exists())
                .andExpect(jsonPath("$.invalidFields.engineCc").exists())
                .andExpect(jsonPath("$.invalidFields.startingPrice").exists());
    }

    @Test
    void shouldListMotorcyclesWithFilters() throws Exception {
        MotorcycleCreateRequest req1 = new MotorcycleCreateRequest(
                "Hunter 350", "Modern Classic", 349, new BigDecimal("19990.00"), null, true, null
        );
        MotorcycleCreateRequest req2 = new MotorcycleCreateRequest(
                "Classic 350", "Classic", 349, new BigDecimal("20990.00"), null, true, null
        );

        mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/motorcycles")
                        .param("family", "Classic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].modelName", is("Classic 350")));
    }

    @Test
    void shouldGetMotorcycleById() throws Exception {
        MotorcycleCreateRequest request = new MotorcycleCreateRequest(
                "Shotgun 650", "Custom", 648, new BigDecimal("32990.00"), "Custom bobber style", true, null
        );

        String response = mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/api/v1/motorcycles/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.modelName", is("Shotgun 650")));
    }

    @Test
    void shouldReturn404WhenMotorcycleNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/motorcycles/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Resource Not Found")));
    }

    @Test
    void shouldUpdateMotorcycle() throws Exception {
        MotorcycleCreateRequest request = new MotorcycleCreateRequest(
                "Interceptor 650", "Roadster", 648, new BigDecimal("29990.00"), null, true, null
        );

        String response = mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        MotorcycleUpdateRequest updateReq = new MotorcycleUpdateRequest(
                "Interceptor 650 Twin", "Roadster", 648, new BigDecimal("30990.00"), "Updated description", true
        );

        mockMvc.perform(put("/api/v1/motorcycles/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelName", is("Interceptor 650 Twin")))
                .andExpect(jsonPath("$.startingPrice", is(30990.00)))
                .andExpect(jsonPath("$.description", is("Updated description")));
    }

    @Test
    void shouldToggleMotorcycleStatus() throws Exception {
        MotorcycleCreateRequest request = new MotorcycleCreateRequest(
                "Continental GT 650", "Cafe Racer", 648, new BigDecimal("31990.00"), null, true, null
        );

        String response = mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(patch("/api/v1/motorcycles/" + id + "/toggle-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active", is(false)));

        mockMvc.perform(patch("/api/v1/motorcycles/" + id + "/toggle-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void shouldDeleteMotorcycle() throws Exception {
        MotorcycleCreateRequest request = new MotorcycleCreateRequest(
                "Himalayan 450", "Adventure", 452, new BigDecimal("28990.00"), null, true, null
        );

        String response = mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(delete("/api/v1/motorcycles/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/motorcycles/" + id))
                .andExpect(status().isNotFound());
    }
}
