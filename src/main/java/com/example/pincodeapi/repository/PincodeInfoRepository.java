package com.example.pincodeapi.repository;

import com.example.pincodeapi.model.PincodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PincodeInfoRepository extends JpaRepository<PincodeInfo, String> {
}