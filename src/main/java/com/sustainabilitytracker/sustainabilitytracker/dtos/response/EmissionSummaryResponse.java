package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmissionSummaryResponse {
    private BigDecimal totalCO2 = BigDecimal.ZERO;
    private BigDecimal totalCH4 = BigDecimal.ZERO;
    private BigDecimal totalN2O = BigDecimal.ZERO;
    private BigDecimal totalEmissions = BigDecimal.ZERO;
    private String period;
    private Integer recordCount = 0;
}
