package com.example.test_alfa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.example.test_alfa.service.DogService;

@RestController
@RequestMapping("")
public class ApiDogController {

    private final DogService dogService;

    public ApiDogController(DogService dogService) {
        this.dogService = dogService;
    }

 @GetMapping("/breeds")
    public Map<String, Object> getAllBreeds() {
        return dogService.transformBreedsResponse();
    }

    @GetMapping("/terrier/images")
    public Map<String, List<String>> getTerrierImages() throws ExecutionException, InterruptedException {
        return dogService.getTerrierImages();
    }

    @GetMapping("/sheepdog/images")
    public Map<String, List<String>> getSheepdogImages() throws ExecutionException, InterruptedException {
        return dogService.getSheepdogImages();
    }

    @GetMapping("/shiba/images")
    public Map<String, List<String>> getShibaImages() {
        return dogService.getShibaImages();
    }

    // create
   

}
