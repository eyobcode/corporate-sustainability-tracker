package com.sustainabilitytracker.sustainabilitytracker.projection;

import java.math.BigDecimal;

public interface WaterTotalsProjection {
    BigDecimal getTotalConsumedLiters();
    BigDecimal getTotalRecycledLiters();
    BigDecimal getRecyclingRate();
    Long getRecordCount();
}
