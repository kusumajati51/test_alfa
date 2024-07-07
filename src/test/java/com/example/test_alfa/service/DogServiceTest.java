package com.example.test_alfa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class DogServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplate restTemplateWithShortTimeout;

    @InjectMocks
    private DogService dogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBreeds() {
        Map<String, Object> mockResponse = Map.of("message", Map.of("terrier", List.of("american", "australian")));
        when(restTemplate.getForObject(anyString(), (Class<Map<String, Object>>) any())).thenReturn(mockResponse);

        Map<String, Object> result = dogService.getAllBreeds();
        assertEquals(mockResponse, result);
    }

    @Test
    void testGetTerrierImages() throws Exception {
        Map<String, Object> subBreedsResponse = Map.of("message", List.of("terrier-silky", "terrier-bedlington"));
        when(restTemplateWithShortTimeout.getForObject(anyString(), (Class<Map<String, Object>>) any()))
                .thenReturn(subBreedsResponse);

        Map<String, Object> imagesResponse = Map.of("message", List.of("terrier-silky", "terrier-bedlington"));
        when(restTemplate.getForObject(anyString(), (Class<Map<String, Object>>) any())).thenReturn(imagesResponse);

        Map<String, List<String>>result = dogService.getTerrierImages();
        assertEquals(2, result.size());
    }


    @Test
    void testGetShibaImages() {
        // Mock response from the API
       

        // Call the method under test
        Map<String, List<String>> result = dogService.getShibaImages();

        // Print the result for debugging
        System.out.println("Result: " + result);

        // Assert that the result contains the key "shiba"
        assertNotNull(result, "result should not be null");
    }

    @Test
    void testGetSheepdogImages() throws Exception {


        Map<String, List<String>> result = dogService.getSheepdogImages();
        assertNotNull(result, "result should not be null");

        assertEquals(2, result.size());

    }
}