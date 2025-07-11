package com.example.pincodeapi.service;

import com.example.pincodeapi.client.GoogleDirectionsClient;
import com.example.pincodeapi.client.GoogleGeocodingClient;
import com.example.pincodeapi.exception.InvalidParameterException;
import com.example.pincodeapi.model.RouteResponse;
import com.example.pincodeapi.repository.RouteRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RouteService {

    private final GoogleGeocodingClient geocodingClient;
    private final GoogleDirectionsClient directionsClient;
    private final RouteRepository routeRepository;

    public RouteService(GoogleGeocodingClient geocodingClient,
                        GoogleDirectionsClient directionsClient,
                        RouteRepository routeRepository) {
        this.geocodingClient = geocodingClient;
        this.directionsClient = directionsClient;
        this.routeRepository = routeRepository;
    }

    @Cacheable(value = "routes", key = "#pincode1 + '-' + #pincode2")
    public RouteResponse getRouteBetweenPincodes(String pincode1, String pincode2) {

        if (pincode1 == null || pincode2 == null || pincode1.isEmpty() || pincode2.isEmpty()) {
            throw new InvalidParameterException("Pincodes cannot be null or empty");
        }

        Optional<RouteResponse> cached = routeRepository.findByPincode1AndPincode2(pincode1, pincode2);
        if (cached.isPresent()) return cached.get();

        String origin = geocodingClient.getLatLng(pincode1);
        String destination = geocodingClient.getLatLng(pincode2);

        RouteResponse result = directionsClient.getDirections(pincode1, pincode2, origin, destination);
        return routeRepository.save(result);
    }
}