package com.sustainabilitytracker.sustainabilitytracker.dtos.request;

import com.sustainabilitytracker.sustainabilitytracker.enums.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportRequest {

    @NotNull(message = "Company is required")
    private Long companyId;

    @NotNull(message = "Period start is required")
    private LocalDate periodStart;

    @NotNull(message = "Period end is required")
    private LocalDate periodEnd;

    private ReportType reportType;

    private String fileFormat; // PDF or EXCEL

    private String reportTitle;
}