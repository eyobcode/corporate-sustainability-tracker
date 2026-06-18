package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.SocialData;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.projection.SocialTotalsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SocialRepository extends JpaRepository<SocialData, Long> {

    boolean existsByDepartmentIdAndRecordedAtAndStatus(
            Long departmentId,
            LocalDate recordedAt,
            DataStatus status
    );

    List<SocialData> findByCompanyId(Long companyId);
    List<SocialData> findBySubmittedBy_Id(Long userId);
    List<SocialData> findByDepartmentId(Long departmentId);

    @Query("""
        SELECT COUNT(s.id) AS recordCount
        FROM SocialData s
        WHERE s.company.id = :companyId
          AND s.status = com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus.APPROVED
          AND s.recordedAt BETWEEN :start AND :end
    """)
    SocialTotalsProjection getTotalsByCompanyAndPeriod(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
        SELECT COALESCE(SUM(s.safetyIncidents), 0)
        FROM SocialData s
        WHERE s.company.id = :companyId
          AND s.recordedAt BETWEEN :start AND :end
          AND s.status = com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus.APPROVED
    """)
    int getTotalSafetyIncidents(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
            SELECT COALESCE(
                SUM(s.femaleWorkers) * 100.0 / NULLIF(SUM(s.totalWorkers), 0),
                0
            )
            FROM SocialData s
            WHERE s.company.id = :companyId
            AND s.recordedAt BETWEEN :start AND :end
            AND s.status = com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus.APPROVED
            """)
    double getAverageFemaleRatio(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
        SELECT COALESCE(AVG(s.trainingHours), 0)
        FROM SocialData s
        WHERE s.company.id = :companyId
          AND s.recordedAt BETWEEN :start AND :end
          AND s.status = com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus.APPROVED
    """)
    BigDecimal getAverageTrainingHours(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
            SELECT AVG(s.satisfactionScore)
            FROM SocialData s
            WHERE s.company.id = :companyId
            AND s.recordedAt BETWEEN :start AND :end
            AND s.status = com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus.APPROVED
            """)
    BigDecimal getAverageSatisfactionScore(
            @Param("companyId") Long companyId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
            SELECT COUNT(s)
            FROM SocialData s
            WHERE s.company.id = :companyId
            AND s.status = :status
            """)
    int countByCompanyIdAndStatus(
            @Param("companyId") Long companyId,
            @Param("status") DataStatus status
    );
}