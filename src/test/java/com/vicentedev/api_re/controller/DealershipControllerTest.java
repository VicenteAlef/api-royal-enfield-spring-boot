package com.vicentedev.api_re.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import com.vicentedev.api_re.dto.request.DealershipCreateRequest;
import com.vicentedev.api_re.dto.request.DealershipUpdateRequest;
import com.vicentedev.api_re.repository.DealershipRepository;
import com.vicentedev.api_re.repository.TestRideRepository;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DealershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DealershipRepository dealershipRepository;

    @Autowired
    private TestRideRepository testRideRepository;

    @BeforeEach
    void setUp() {
        testRideRepository.deleteAll();
        dealershipRepository.deleteAll();
    }

    @Test
    void shouldCreateDealershipSuccessfully() throws Exception {
        DealershipCreateRequest request = new DealershipCreateRequest(
                "Royal Enfield Moema",
                "Sao Paulo",
                "SP",
                "Av. Ibirapuera, 2907 - Moema",
                "(11) 5051-0000",
                "moema@royalenfield.com.br"
        );

        mockMvc.perform(post("/api/v1/dealerships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("Royal Enfield Moema")))
                .andExpect(jsonPath("$.city", is("Sao Paulo")))
                .andExpect(jsonPath("$.state", is("SP")))
                .andExpect(jsonPath("$.phone", is("(11) 5051-0000")))
                .andExpect(jsonPath("$.email", is("moema@royalenfield.com.br")));
    }

    @Test
    void shouldReturnValidationErrorsWhenCreatingInvalidDealership() throws Exception {
        DealershipCreateRequest invalidRequest = new DealershipCreateRequest(
                "",
                "",
                "XYZ",
                "",
                null,
                "invalid-email"
        );

        mockMvc.perform(post("/api/v1/dealerships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Validation Error")))
                .andExpect(jsonPath("$.invalidFields.name").exists())
                .andExpect(jsonPath("$.invalidFields.city").exists())
                .andExpect(jsonPath("$.invalidFields.state").exists())
                .andExpect(jsonPath("$.invalidFields.address").exists())
                .andExpect(jsonPath("$.invalidFields.email").exists());
    }

    @Test
    void shouldListDealershipsWithFilters() throws Exception {
        DealershipCreateRequest req1 = new DealershipCreateRequest(
                "Royal Enfield Moema", "Sao Paulo", "SP", "Av. Ibirapuera, 2907", null, null
        );
        DealershipCreateRequest req2 = new DealershipCreateRequest(
                "Royal Enfield Barra", "Rio de Janeiro", "RJ", "Av. das Americas, 5000", null, null
        );

        mockMvc.perform(post("/api/v1/dealerships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/dealerships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/dealerships")
                        .param("state", "RJ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Royal Enfield Barra")))
                .andExpect(jsonPath("$.content[0].state", is("RJ")));
    }

    @Test
    void shouldGetDealershipById() throws Exception {
        DealershipCreateRequest request = new DealershipCreateRequest(
                "Royal Enfield Curitiba", "Curitiba", "PR", "Rua General Mario Tourinho, 1200", "(41) 3030-0000", "curitiba@re.com"
        );

        String response = mockMvc.perform(post("/api/v1/dealerships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/api/v1/dealerships/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is("Royal Enfield Curitiba")))
                .andExpect(jsonPath("$.city", is("Curitiba")));
    }

    @Test
    void shouldUpdateDealership() throws Exception {
        DealershipCreateRequest request = new DealershipCreateRequest(
                "Royal Enfield BH", "Belo Horizonte", "MG", "Av. Baracao, 100", null, null
        );

        String response = mockMvc.perform(post("/api/v1/dealerships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        DealershipUpdateRequest updateReq = new DealershipUpdateRequest(
                "Royal Enfield BH Savassi", "Belo Horizonte", "MG", "Av. do Contorno, 5000", "(31) 3333-0000", "savassi@re.com"
        );

        mockMvc.perform(put("/api/v1/dealerships/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Royal Enfield BH Savassi")))
                .andExpect(jsonPath("$.address", is("Av. do Contorno, 5000")))
                .andExpect(jsonPath("$.phone", is("(31) 3333-0000")));
    }

    @Test
    void shouldDeleteDealership() throws Exception {
        DealershipCreateRequest request = new DealershipCreateRequest(
                "Royal Enfield Campinas", "Campinas", "SP", "Av. Norte-Sul, 200", null, null
        );

        String response = mockMvc.perform(post("/api/v1/dealerships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(delete("/api/v1/dealerships/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/dealerships/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDealershipNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/dealerships/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Resource Not Found")));
    }
}
