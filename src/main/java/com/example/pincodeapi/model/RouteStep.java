package com.example.pincodeapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteStep {
    private String instruction;
    private String distance;
    private String duration;
}
