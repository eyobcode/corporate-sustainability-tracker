package com.sustainabilitytracker.sustainabilitytracker.projection;

import java.math.BigDecimal;

public interface GovernanceTotalsProjection {
    Long getRecordCount();
    BigDecimal getAverageComplianceScore();
    Integer getTotalPolicies();
    Integer getTotalViolations();
    BigDecimal getAverageBoardDiversity();
}
