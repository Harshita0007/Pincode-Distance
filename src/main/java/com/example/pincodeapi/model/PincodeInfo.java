package com.example.pincodeapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PincodeInfo {

    @Id
    private String pincode;

    private double latitude;
    private double longitude;

    @Column(columnDefinition = "TEXT")
    private String polygon; // Can store bounding box or GeoJSON if needed
}
