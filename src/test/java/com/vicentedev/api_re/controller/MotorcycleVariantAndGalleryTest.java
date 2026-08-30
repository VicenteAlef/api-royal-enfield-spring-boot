package com.vicentedev.api_re.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import com.vicentedev.api_re.dto.request.MotorcycleCreateRequest;
import com.vicentedev.api_re.dto.request.MotorcycleGalleryRequest;
import com.vicentedev.api_re.dto.request.MotorcycleVariantRequest;
import com.vicentedev.api_re.dto.request.TechnicalSpecRequest;
import com.vicentedev.api_re.repository.MotorcycleGalleryRepository;
import com.vicentedev.api_re.repository.MotorcycleRepository;
import com.vicentedev.api_re.repository.MotorcycleVariantRepository;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class MotorcycleVariantAndGalleryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    @Autowired
    private MotorcycleVariantRepository variantRepository;

    @Autowired
    private MotorcycleGalleryRepository galleryRepository;

    private UUID motorcycleId;

    @BeforeEach
    void setUp() throws Exception {
        galleryRepository.deleteAll();
        variantRepository.deleteAll();
        motorcycleRepository.deleteAll();

        MotorcycleCreateRequest request = new MotorcycleCreateRequest(
                "Super Meteor 650", "Cruiser", 648, new BigDecimal("33990.00"), "Cruiser", true, null
        );

        String response = mockMvc.perform(post("/api/v1/motorcycles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        motorcycleId = UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    @Test
    void shouldCreateVariantViaJson() throws Exception {
        MotorcycleVariantRequest variantReq = new MotorcycleVariantRequest(
                "Astral", "Astral Black", "#000000", new BigDecimal("33990.00"),
                null, "Standard accessories", true
        );

        mockMvc.perform(post("/api/v1/motorcycles/" + motorcycleId + "/variants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(variantReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.variantName", is("Astral")))
                .andExpect(jsonPath("$.colorName", is("Astral Black")));
    }

    @Test
    void shouldCreateVariantWithMultipartImage() throws Exception {
        MotorcycleVariantRequest variantReq = new MotorcycleVariantRequest(
                "Celestial", "Celestial Red", "#FF0000", new BigDecimal("35990.00"),
                null, "Windshield, Touring Seat", true
        );

        MockMultipartFile dataPart = new MockMultipartFile(
                "data", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(variantReq)
        );

        MockMultipartFile imagePart = new MockMultipartFile(
                "image", "celestial.png", "image/png", "fake image content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/motorcycles/" + motorcycleId + "/variants")
                        .file(dataPart)
                        .file(imagePart))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.variantName", is("Celestial")))
                .andExpect(jsonPath("$.imageUrl", notNullValue()));
    }

    @Test
    void shouldAddGalleryImageViaFile() throws Exception {
        MockMultipartFile filePart = new MockMultipartFile(
                "file", "gallery1.jpg", "image/jpeg", "fake image content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/motorcycles/" + motorcycleId + "/gallery")
                        .file(filePart)
                        .param("caption", "Front Angle View")
                        .param("displayOrder", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.caption", is("Front Angle View")))
                .andExpect(jsonPath("$.displayOrder", is(1)))
                .andExpect(jsonPath("$.imageUrl", notNullValue()));
    }

    @Test
    void shouldCreateOrUpdateTechnicalSpec() throws Exception {
        TechnicalSpecRequest specReq = new TechnicalSpecRequest(
                "47 hp", "52 Nm", new BigDecimal("241.00"),
                new BigDecimal("15.70"), 740, "6 Speed",
                "320mm Disc", "300mm Disc", "Air-Oil"
        );

        mockMvc.perform(put("/api/v1/motorcycles/" + motorcycleId + "/technical-spec")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.powerHp", is("47 hp")))
                .andExpect(jsonPath("$.torqueNm", is("52 Nm")));

        mockMvc.perform(get("/api/v1/motorcycles/" + motorcycleId + "/technical-spec"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.powerHp", is("47 hp")));
    }

    @Test
    void shouldUpdateGalleryImage() throws Exception {
        MockMultipartFile filePart = new MockMultipartFile(
                "file", "gallery1.jpg", "image/jpeg", "fake image content".getBytes()
        );

        String createRes = mockMvc.perform(multipart("/api/v1/motorcycles/" + motorcycleId + "/gallery")
                        .file(filePart)
                        .param("caption", "Original Caption")
                        .param("displayOrder", "1"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String galleryId = objectMapper.readTree(createRes).get("id").asText();

        MockMultipartFile newFilePart = new MockMultipartFile(
                "file", "gallery_updated.png", "image/png", "new image content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/gallery/" + galleryId)
                        .file(newFilePart)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .param("caption", "Updated Caption")
                        .param("displayOrder", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(galleryId)))
                .andExpect(jsonPath("$.caption", is("Updated Caption")))
                .andExpect(jsonPath("$.displayOrder", is(2)));
    }
}
