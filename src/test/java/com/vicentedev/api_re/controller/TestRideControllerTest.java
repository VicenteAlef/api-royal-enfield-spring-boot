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
import com.vicentedev.api_re.dto.request.MotorcycleCreateRequest;
import com.vicentedev.api_re.dto.request.TestRideCreateRequest;
import com.vicentedev.api_re.dto.request.TestRideStatusUpdateRequest;
import com.vicentedev.api_re.entity.TestRideStatus;
import com.vicentedev.api_re.repository.DealershipRepository;
import com.vicentedev.api_re.repository.MotorcycleGalleryRepository;
import com.vicentedev.api_re.repository.MotorcycleRepository;
import com.vicentedev.api_re.repository.MotorcycleVariantRepository;
import com.vicentedev.api_re.repository.TestRideRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestRideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRideRepository testRideRepository;

    @Autowired
    private DealershipRepository dealershipRepository;

    @Autowired
    private MotorcycleGalleryRepository galleryRepository;

    @Autowired
    private MotorcycleVariantRepository variantRepository;

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    private UUID motorcycleId;
    private UUID dealershipId;

    @BeforeEach
    void setUp() throws Exception {
        testRideRepository.deleteAll();
        galleryRepository.deleteAll();
        variantRepository.deleteAll();
        motorcycleRepository.deleteAll();
        dealershipRepository.deleteAll();

        MotorcycleCreateRequest motoReq = new MotorcycleCreateRequest(
                "Hunter 350", "Modern Classic", 349, new BigDecimal("19990.00"), "Urban Roadster", true, null
        );
        String motoRes = mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(motoReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        motorcycleId = UUID.fromString(objectMapper.readTree(motoRes).get("id").asText());

        DealershipCreateRequest dealerReq = new DealershipCreateRequest(
                "Royal Enfield Pinheiros", "Sao Paulo", "SP", "Av. Reboucas, 1000", "(11) 3030-1000", "pinheiros@re.com"
        );
        String dealerRes = mockMvc.perform(post("/api/v1/dealerships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dealerReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        dealershipId = UUID.fromString(objectMapper.readTree(dealerRes).get("id").asText());
    }

    @Test
    void shouldCreateTestRideSuccessfully() throws Exception {
        TestRideCreateRequest request = new TestRideCreateRequest(
                "Carlos Silva",
                "carlos.silva@email.com",
                "(11) 98765-4321",
                OffsetDateTime.now().plusDays(5),
                motorcycleId,
                null,
                dealershipId
        );

        mockMvc.perform(post("/api/v1/test-rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerName", is("Carlos Silva")))
                .andExpect(jsonPath("$.customerEmail", is("carlos.silva@email.com")))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.motorcycle.modelName", is("Hunter 350")))
                .andExpect(jsonPath("$.dealership.name", is("Royal Enfield Pinheiros")));
    }

    @Test
    void shouldFailWhenPreferredDateIsInThePast() throws Exception {
        TestRideCreateRequest request = new TestRideCreateRequest(
                "Carlos Silva",
                "carlos.silva@email.com",
                "(11) 98765-4321",
                OffsetDateTime.now().minusDays(1),
                motorcycleId,
                null,
                dealershipId
        );

        mockMvc.perform(post("/api/v1/test-rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Validation Error")))
                .andExpect(jsonPath("$.invalidFields.preferredDate").exists());
    }

    @Test
    void shouldFailWhenMotorcycleOrDealershipNotFound() throws Exception {
        TestRideCreateRequest request = new TestRideCreateRequest(
                "Carlos Silva",
                "carlos.silva@email.com",
                "(11) 98765-4321",
                OffsetDateTime.now().plusDays(3),
                UUID.randomUUID(),
                null,
                dealershipId
        );

        mockMvc.perform(post("/api/v1/test-rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Resource Not Found")));
    }

    @Test
    void shouldListTestRidesWithFilters() throws Exception {
        TestRideCreateRequest req1 = new TestRideCreateRequest(
                "Ana Costa", "ana@email.com", "(11) 91111-1111",
                OffsetDateTime.now().plusDays(2), motorcycleId, null, dealershipId
        );
        TestRideCreateRequest req2 = new TestRideCreateRequest(
                "Bruno Lima", "bruno@email.com", "(11) 92222-2222",
                OffsetDateTime.now().plusDays(3), motorcycleId, null, dealershipId
        );

        mockMvc.perform(post("/api/v1/test-rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/test-rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/test-rides")
                        .param("customerEmail", "ana@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].customerName", is("Ana Costa")));
    }

    @Test
    void shouldUpdateTestRideStatus() throws Exception {
        TestRideCreateRequest request = new TestRideCreateRequest(
                "Marcos Paulo", "marcos@email.com", "(11) 93333-3333",
                OffsetDateTime.now().plusDays(4), motorcycleId, null, dealershipId
        );

        String response = mockMvc.perform(post("/api/v1/test-rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        TestRideStatusUpdateRequest statusReq = new TestRideStatusUpdateRequest(TestRideStatus.CONFIRMED);

        mockMvc.perform(patch("/api/v1/test-rides/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CONFIRMED")));
    }

    @Test
    void shouldCancelTestRide() throws Exception {
        TestRideCreateRequest request = new TestRideCreateRequest(
                "Fernanda Souza", "fernanda@email.com", "(11) 94444-4444",
                OffsetDateTime.now().plusDays(5), motorcycleId, null, dealershipId
        );

        String response = mockMvc.perform(post("/api/v1/test-rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(patch("/api/v1/test-rides/" + id + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CANCELLED")));
    }

    @Test
    void shouldDeleteTestRide() throws Exception {
        TestRideCreateRequest request = new TestRideCreateRequest(
                "Diego Ramos", "diego@email.com", "(11) 95555-5555",
                OffsetDateTime.now().plusDays(6), motorcycleId, null, dealershipId
        );

        String response = mockMvc.perform(post("/api/v1/test-rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(delete("/api/v1/test-rides/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/test-rides/" + id))
                .andExpect(status().isNotFound());
    }
}
