package com.sustainabilitytracker.sustainabilitytracker;

import com.sustainabilitytracker.sustainabilitytracker.controllers.EmissionController;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.EmissionRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EmissionResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EmissionSummaryResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.EmissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmissionController.class)
class EmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmissionService emissionService;

    @Test
    void submitEmission_ShouldReturn201Created() throws Exception {
        EmissionResponse response = new EmissionResponse();
        response.setId(1L);

        when(emissionService.submitEmission(any(EmissionRequest.class))).thenReturn(response);

        String json = """
                {
                    "companyId": 1,
                    "departmentId": 1,
                    "co2Amount": 1250.75,
                    "recordedAt": "2025-06-01",
                    "notes": "Factory production"
                }
                """;

        mockMvc.perform(post("/api/v1/emissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void submitForApproval_ShouldReturn200() throws Exception {
        when(emissionService.submitForApproval(1L)).thenReturn(new EmissionResponse());

        mockMvc.perform(put("/api/v1/emissions/1/submit"))
                .andExpect(status().isOk());
    }

    @Test
    void approveEmission_ShouldReturn200() throws Exception {
        when(emissionService.approveEmission(1L)).thenReturn(new EmissionResponse());

        mockMvc.perform(put("/api/v1/emissions/1/approve"))
                .andExpect(status().isOk());
    }

    @Test
    void rejectEmission_ShouldReturn200() throws Exception {
        when(emissionService.rejectEmission(1L, "Invalid data")).thenReturn(new EmissionResponse());

        String json = """
                {
                    "reason": "Invalid data"
                }
                """;

        mockMvc.perform(put("/api/v1/emissions/1/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void getEmissionsByCompany_ShouldReturn200() throws Exception {
        when(emissionService.getEmissionByCompany(1L))
                .thenReturn(List.of(new EmissionResponse()));

        mockMvc.perform(get("/api/v1/emissions/company/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getEmissionSummary_ShouldReturn200() throws Exception {
        when(emissionService.getEmissionSummary(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(new EmissionSummaryResponse());

        mockMvc.perform(get("/api/v1/emissions/company/1/summary")
                        .param("startDate", "2025-06-01")
                        .param("endDate", "2025-06-30"))
                .andExpect(status().isOk());
    }
}