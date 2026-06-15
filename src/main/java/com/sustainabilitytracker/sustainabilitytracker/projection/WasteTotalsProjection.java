package com.sustainabilitytracker.sustainabilitytracker.projection;

import java.math.BigDecimal;

public interface WasteTotalsProjection {
    BigDecimal getTotalKg();
    BigDecimal getTotalRecycledKg();
    BigDecimal getTotalHazardousKg();
    BigDecimal getRecyclingRate();
    Long getRecordCount();
}
