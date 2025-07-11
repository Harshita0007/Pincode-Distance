package com.example.pincodeapi.repository;

import com.example.pincodeapi.model.RouteResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<RouteResponse, Long> {
    Optional<RouteResponse> findByPincode1AndPincode2(String pincode1, String pincode2);
}

