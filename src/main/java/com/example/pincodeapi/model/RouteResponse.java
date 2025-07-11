package com.example.pincodeapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pincode1;
    private String pincode2;
    private String distance;
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String fullRouteJson;

    @Column(columnDefinition = "TEXT")
    private String polyline;

    @Transient
    private List<RouteStep> steps;
}