package com.example.pincodeapi.controller;

import com.example.pincodeapi.model.RouteResponse;
import com.example.pincodeapi.model.RouteStep;
import com.example.pincodeapi.service.RouteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping("/route")
    @Cacheable(value = "routes", key = "#pincode1 + '-' + #pincode2")
    public ResponseEntity<?> getRoute(@RequestParam String pincode1, @RequestParam String pincode2) {
        RouteResponse route = routeService.getRouteBetweenPincodes(pincode1, pincode2);

        Map<String, Object> response = new HashMap<>();
        response.put("id", route.getId());
        response.put("pincode1", route.getPincode1());
        response.put("pincode2", route.getPincode2());
        response.put("distance", route.getDistance());
        response.put("duration", route.getDuration());

        ObjectMapper mapper = new ObjectMapper();
        try {
            List<RouteStep> steps = mapper.readValue(route.getFullRouteJson(), new TypeReference<>() {});
            response.put("steps", steps);
        } catch (Exception e) {
            response.put("steps", List.of("Could not parse route steps."));
        }

        return ResponseEntity.ok(response);
    }
}
