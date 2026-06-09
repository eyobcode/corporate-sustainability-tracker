package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EnergySummaryResponse {
    private BigDecimal totalKwh;
    private BigDecimal totalRenewableKwh;
    private BigDecimal averageKwh;
    private String period;
    private Integer recordCount;
}
