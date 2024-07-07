package com.example.test_alfa.controller;

import com.example.test_alfa.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiDogController.class)
public class ApiDogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DogService dogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBreeds() throws Exception {
        // Mock response from the service
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> breeds = new HashMap<>();
        breeds.put("breed1", Arrays.asList("subbreed1", "subbreed2"));
        mockResponse.put("message", breeds);
        mockResponse.put("status", "success");

        when(dogService.transformBreedsResponse()).thenReturn(mockResponse);

        mockMvc.perform(get("/breeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message.breed1", hasSize(2)))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testGetTerrierImages() throws Exception {
        // Mock response from the service
        Map<String, List<String>> mockResponse = new HashMap<>();
        mockResponse.put("terrier-sub1", Arrays.asList("image1", "image2"));
        mockResponse.put("terrier-sub2", Arrays.asList("image3", "image4"));

        when(dogService.getTerrierImages()).thenReturn(mockResponse);

        mockMvc.perform(get("/terrier/images"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['terrier-sub1']", hasSize(2)))
                .andExpect(jsonPath("$.['terrier-sub2']", hasSize(2)));
    }

    @Test
    void testGetSheepdogImages() throws Exception {
        // Mock response from the service
        Map<String, List<String>> mockResponse = new HashMap<>();
        mockResponse.put("sheepdog-sub1", Arrays.asList("image1", "image2"));
        mockResponse.put("sheepdog-sub2", Arrays.asList("image3", "image4"));

        when(dogService.getSheepdogImages()).thenReturn(mockResponse);

        mockMvc.perform(get("/sheepdog/images"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['sheepdog-sub1']", hasSize(2)))
                .andExpect(jsonPath("$.['sheepdog-sub2']", hasSize(2)));
    }

    @Test
    void testGetShibaImages() throws Exception {
        // Mock response from the service
        Map<String, List<String>> mockResponse = new HashMap<>();
        mockResponse.put("shiba", Arrays.asList("image1", "image2", "image3"));

        when(dogService.getShibaImages()).thenReturn(mockResponse);

        mockMvc.perform(get("/shiba/images"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shiba", hasSize(3)));
    }
}
