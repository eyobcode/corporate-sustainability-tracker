package com.sustainabilitytracker.sustainabilitytracker.dtos.response;

import com.sustainabilitytracker.sustainabilitytracker.enums.ReportType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class ReportResponse {

    private Long id;
    private Long companyId;
    private String companyName;
    private Long scoreId;
    private String reportTitle;
    private ReportType reportType;
    private String fileFormat;
    private String downloadUrl;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Instant generatedAt;
}
