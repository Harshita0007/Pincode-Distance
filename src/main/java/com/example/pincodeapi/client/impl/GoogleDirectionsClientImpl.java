package com.example.pincodeapi.client.impl;

import com.example.pincodeapi.client.GoogleDirectionsClient;
import com.example.pincodeapi.model.RouteResponse;
import com.example.pincodeapi.model.RouteStep;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleDirectionsClientImpl implements GoogleDirectionsClient {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GoogleDirectionsClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RouteResponse getDirections(String pincode1, String pincode2, String origin, String destination) {
        String directionsUrl = String.format(
                "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",
                origin, destination, apiKey
        );

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(directionsUrl, JsonNode.class);
        JsonNode body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Google Directions API response body is null");
        }
        JsonNode route = body.path("routes").get(0);
        JsonNode leg = route.path("legs").get(0);
        JsonNode steps = leg.path("steps");

        List<RouteStep> readableSteps = new ArrayList<>();
        for (JsonNode step : steps) {
            String instruction = Jsoup.parse(step.path("html_instructions").asText()).text();
            String distance = step.path("distance").path("text").asText();
            String duration = step.path("duration").path("text").asText();
            readableSteps.add(new RouteStep(instruction, distance, duration));
        }

        try {
            String readableJson = new ObjectMapper().writeValueAsString(readableSteps);

            return RouteResponse.builder()
                    .pincode1(pincode1)
                    .pincode2(pincode2)
                    .distance(leg.path("distance").path("text").asText())
                    .duration(leg.path("duration").path("text").asText())
                    .polyline(route.path("overview_polyline").path("points").asText())
                    .fullRouteJson(readableJson)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse directions", e);
        }
    }
}