package com.sustainabilitytracker.sustainabilitytracker.repositories;

import com.sustainabilitytracker.sustainabilitytracker.entities.AuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<AuditRecord, Long> {

    List<AuditRecord> findByReportIdOrderByCreatedAtDesc(Long reportId);

    List<AuditRecord> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
}