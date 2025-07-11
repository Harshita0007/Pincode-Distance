package com.example.pincodeapi;

import com.example.pincodeapi.repository.RouteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PincodeapiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Optional: clear database or seed test data
    }

    @Test
    void testFirstCall141106To110060() throws Exception {
        mockMvc.perform(get("/api/route")
                        .param("pincode1", "141106")
                        .param("pincode2", "110060"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pincode1").value("141106"))
                .andExpect(jsonPath("$.pincode2").value("110060"))
                .andExpect(jsonPath("$.distance", not(emptyOrNullString())))
                .andExpect(jsonPath("$.duration", not(emptyOrNullString())))
                .andExpect(jsonPath("$.steps").isArray());
    }

    @Test
    void testSecondCall141106To560023() throws Exception {
        mockMvc.perform(get("/api/route")
                        .param("pincode1", "141106")
                        .param("pincode2", "560023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pincode1").value("141106"))
                .andExpect(jsonPath("$.pincode2").value("560023"))
                .andExpect(jsonPath("$.steps").isArray());
    }

    @Test
    void testThirdCall141106ToInvalid11006() throws Exception {
        mockMvc.perform(get("/api/route")
                        .param("pincode1", "141106")
                        .param("pincode2", "11006"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingParameters() throws Exception {
        mockMvc.perform(get("/api/route")
                        .param("pincode1", "141106")) // missing pincode2
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidPincodeFormat() throws Exception {
        mockMvc.perform(get("/api/route")
                        .param("pincode1", "ABCDE1")
                        .param("pincode2", "12345Z"))
                .andExpect(status().isBadRequest());
    }
}
