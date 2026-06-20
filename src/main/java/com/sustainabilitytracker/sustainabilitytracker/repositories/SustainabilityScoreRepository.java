package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SustainabilityScoreRepository extends JpaRepository<SustainabilityScore, Long> {
    List<SustainabilityScore> findByCompanyIdOrderByPeriodStartDesc(Long companyId);

    Optional<SustainabilityScore> findTopByCompanyIdOrderByCalculatedAtDesc(Long companyId);
    Optional<SustainabilityScore> findTopByOrderByTotalScoreDesc();
    Optional<SustainabilityScore> findTopByOrderByTotalScoreAsc();
    List<SustainabilityScore> findTop6ByCompanyIdOrderByPeriodStartDesc(Long companyId);

    Optional<SustainabilityScore> findByCompanyIdAndPeriodStartAndPeriodEnd(Long companyId, LocalDate periodStart, LocalDate periodEnd);
}
