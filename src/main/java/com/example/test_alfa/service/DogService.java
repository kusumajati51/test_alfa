package com.example.test_alfa.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DogService {
    private final RestTemplate restTemplate;
    private final RestTemplate restTemplateWithShortTimeout;

    public DogService(RestTemplate restTemplate, RestTemplate restTemplateWithShortTimeout) {
        this.restTemplate = restTemplate;
        this.restTemplateWithShortTimeout = restTemplateWithShortTimeout;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllBreeds() {
        Map<String, Object> response = restTemplate.getForObject("https://dog.ceo/api/breeds/list/all", Map.class);
        return response;
    }

    public Map<String, Object> transformBreedsResponse() {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> response = restTemplate.getForObject("https://dog.ceo/api/breeds/list/all", Map.class);
        Map<String, Object> message = (Map<String, Object>) response.get("message");
        for (Map.Entry<String, Object> entry : message.entrySet()) {
            String breed = entry.getKey();
             if (breed.equalsIgnoreCase("terrier")) {
                 try {
                     Map<String, List<String>> restTeries = getTerrierImages();
                    for(Map.Entry<String, List<String>> entryTer: restTeries.entrySet()) {
                        res.put(entryTer.getKey(), entryTer.getValue());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
             } else if (breed.equalsIgnoreCase("sheepdog")) {
                try {
                    Map<String, List<String>> restSheepdogs = getSheepdogImages();
                    for(Map.Entry<String, List<String>> entrySheep: restSheepdogs.entrySet()) {
                        res.put(entrySheep.getKey(), entrySheep.getValue());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (breed.equalsIgnoreCase("shiba")) {
                Map<String, List<String>> restShiba = getShibaImages();
                for (Map.Entry<String, List<String>> entryShiba : restShiba.entrySet()) {
                    res.put(entryShiba.getKey(), entryShiba.getValue());
                }
            } else {
                res.put(breed,  entry.getValue());
            }
        }
        return res;
    }
    @SuppressWarnings("unchecked")
    private Map<String, Object> transformBreeds(Map<String, Object> response) {
        Map<String, Object> message = (Map<String, Object>) response.get("message");
        Map<String, Object> transformed = new HashMap<>();

        for (Map.Entry<String, Object> entry : message.entrySet()) {
            String breed = entry.getKey();
            Object subBreedsObj = entry.getValue();

            if (subBreedsObj instanceof List) {
                List<String> subBreeds = (List<String>) subBreedsObj;
                for (String subBreed : subBreeds) {
                    List<String> formattedSubBreeds = new ArrayList<>();
                    if (breed.equalsIgnoreCase("terrier")) {
                        try {
                            Map<String, Object> responseMap = restTemplate.getForObject(
                                    "https://dog.ceo/api/breed/" + breed + "/" + subBreed + "/images",
                                    Map.class);
                            if (responseMap != null) {
                                List<String> images = (List<String>) responseMap.get("message");
                                formattedSubBreeds.addAll(images);
                            }
                            transformed.put(breed + "-" + subBreed, formattedSubBreeds);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (breed.equalsIgnoreCase("terrier")) {
                        transformed.put(breed + "-" + subBreed, formattedSubBreeds);
                    } else {
                        formattedSubBreeds.add(subBreed);
                        transformed.put(breed, formattedSubBreeds);
                    }
                }
            } else {
                transformed.put(breed, new ArrayList<>());
            }
        }

        response.put("message", transformed);
        response.put("status", "success");
        return response;
    }

    public Map<String, List<String>> getTerrierImages() throws ExecutionException, InterruptedException {
        String url = "https://dog.ceo/api/breed/terrier/list";
        Map<String, Object> subBreedsResponse = restTemplateWithShortTimeout.getForObject(url, Map.class);
        if (subBreedsResponse == null || subBreedsResponse.get("message") == null) {
            return Collections.emptyMap();
        }

        List<String> subBreeds = (List<String>) subBreedsResponse.get("message");

        Map<String, List<String>> result = new HashMap<>();
        List<CompletableFuture<Map.Entry<String, List<String>>>> futures = new ArrayList<>();

        for (String subBreed : subBreeds) {
            String imagesUrl = String.format("https://dog.ceo/api/breed/terrier/%s/images", subBreed);
            CompletableFuture<Map.Entry<String, List<String>>> future = CompletableFuture.supplyAsync(() -> {
                Map<String, Object> imagesResponse = restTemplate.getForObject(imagesUrl, Map.class);
                if (imagesResponse != null) {
                    List<String> images = (List<String>) imagesResponse.get("message");
                    return Map.entry("terrier-" + subBreed, images);
                }
                return Map.entry("terrier-" + subBreed, Collections.<String>emptyList());
            });
            futures.add(future);
        }

        for (CompletableFuture<Map.Entry<String, List<String>>> future : futures) {
            Map.Entry<String, List<String>> entry = future.get();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public Map<String, List<String>> getShibaImages() {
        String url = "https://dog.ceo/api/breed/shiba/images";
        Map<String, Object> response = restTemplateWithShortTimeout.getForObject(url, Map.class);

        if (response == null || response.get("message") == null) {
            return Collections.emptyMap();
        }

        List<String> images = (List<String>) response.get("message");
        List<String> filteredImages = images.stream()
                .filter(img -> {
                    Matcher matcher = Pattern.compile(".*shiba-(\\d+)\\.jpg$").matcher(img);
                    return matcher.matches() && Integer.parseInt(matcher.group(1)) % 2 != 0;
                })
                .collect(Collectors.toList());

        Map<String, List<String>> result = new HashMap<>();
        result.put("shiba", filteredImages);
        return result;
    }

    public Map<String, List<String>> getSheepdogImages() throws ExecutionException, InterruptedException {
        String url = "https://dog.ceo/api/breed/sheepdog/list";
        Map<String, Object> subBreedsResponse = restTemplateWithShortTimeout.getForObject(url, Map.class);
        if (subBreedsResponse == null || subBreedsResponse.get("message") == null) {
            return Collections.emptyMap();
        }

        List<String> subBreeds = (List<String>) subBreedsResponse.get("message");

        Map<String, List<String>> result = new HashMap<>();
        List<CompletableFuture<Map.Entry<String, List<String>>>> futures = new ArrayList<>();

        for (String subBreed : subBreeds) {
            System.out.println(subBreed);
            String imagesUrl = String.format("https://dog.ceo/api/breed/sheepdog/%s/images", subBreed);
            CompletableFuture<Map.Entry<String, List<String>>> future = CompletableFuture.supplyAsync(() -> {
                Map<String, Object> imagesResponse = restTemplate.getForObject(imagesUrl, Map.class);
                if (imagesResponse != null) {
                    List<String> images = (List<String>) imagesResponse.get("message");
                    return Map.entry("sheepdog-" + subBreed, images);
                }
                return Map.entry("sheepdog-" + subBreed, Collections.<String>emptyList());
            });
            futures.add(future);
        }

        for (CompletableFuture<Map.Entry<String, List<String>>> future : futures) {
            Map.Entry<String, List<String>> entry = future.get();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
