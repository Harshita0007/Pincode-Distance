package com.example.pincodeapi.client;

import com.example.pincodeapi.model.RouteResponse;

public interface GoogleDirectionsClient {
    RouteResponse getDirections(String pincode1, String pincode2, String origin, String destination);
}