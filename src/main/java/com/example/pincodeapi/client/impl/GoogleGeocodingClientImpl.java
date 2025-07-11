package com.example.pincodeapi.client.impl;
import com.example.pincodeapi.client.GoogleGeocodingClient;
import com.example.pincodeapi.exception.InvalidPincodeException;
import com.example.pincodeapi.model.PincodeInfo;
import com.example.pincodeapi.repository.PincodeInfoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleGeocodingClientImpl implements GoogleGeocodingClient {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final PincodeInfoRepository pincodeInfoRepository;

    public GoogleGeocodingClientImpl(RestTemplate restTemplate, PincodeInfoRepository pincodeInfoRepository) {
        this.restTemplate = restTemplate;
        this.pincodeInfoRepository = pincodeInfoRepository;
    }

    @Override
    public String getLatLng(String pincode) {
        if (pincode == null || pincode.isEmpty()) {
            throw new IllegalArgumentException("Pincode cannot be null or empty");
        }

        // Validate pincode format (assuming Indian pincodes)
        if (!pincode.matches("\\d{6}")) {
            throw new InvalidPincodeException("Invalid pincode format. Expected 6 digits.");
        }
       
        
        // Check DB first
        if (pincodeInfoRepository.findById(pincode).isPresent()) {
            PincodeInfo info = pincodeInfoRepository.findById(pincode).get();
            return info.getLatitude() + "," + info.getLongitude();
        }

        String geoUrl = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s,IN&key=%s", pincode, apiKey);
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(geoUrl, JsonNode.class);
        JsonNode body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("Google Geocoding API response body is null");
        }
        JsonNode result = body.path("results").get(0);
        JsonNode location = result.path("geometry").path("location");

        double lat = location.path("lat").asDouble();
        double lng = location.path("lng").asDouble();
        String polygon = result.has("geometry") && result.path("geometry").has("bounds")
                ? result.path("geometry").path("bounds").toString() : null;



        // Save to DB
        pincodeInfoRepository.save(PincodeInfo.builder()
                .pincode(pincode)
                .latitude(lat)
                .longitude(lng)
                .polygon(polygon)
                .build());

        return lat + "," + lng;
    }
}