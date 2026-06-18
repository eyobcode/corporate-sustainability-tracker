package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.entities.Company;
import com.sustainabilitytracker.sustainabilitytracker.entities.SustainabilityScore;
import com.sustainabilitytracker.sustainabilitytracker.enums.DataStatus;
import com.sustainabilitytracker.sustainabilitytracker.enums.PeriodType;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.ResourceNotFoundException;
import com.sustainabilitytracker.sustainabilitytracker.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreCalculationService {

    private final EmissionRepository emissionRepository;
    private final EnergyRepository energyRepository;
    private final WaterRepository waterRepository;
    private final WasteRepository wasteRepository;
    private final SocialRepository socialRepository;
    private final GovernanceRepository governanceRepository;
    private final SustainabilityScoreRepository scoreRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public SustainabilityScore calculateAndSaveScore(Long companyId, LocalDate periodStart, LocalDate periodEnd) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        double envScore = calculateEnvironmentScore(companyId, periodStart, periodEnd);
        double socialScore = calculateSocialScore(companyId, periodStart, periodEnd);
        double govScore = calculateGovernanceScore(companyId, periodStart, periodEnd);

        double totalScore = (envScore * 0.40) + (socialScore * 0.30) + (govScore * 0.30);

        String grade = determineGrade(totalScore);

        SustainabilityScore score = new SustainabilityScore();
        score.setCompany(company);
        score.setPeriodStart(periodStart);
        score.setPeriodEnd(periodEnd);
        score.setPeriodType(PeriodType.MONTHLY);
        score.setEnvironmentScore(BigDecimal.valueOf(envScore));
        score.setSocialScore(BigDecimal.valueOf(socialScore));
        score.setGovernanceScore(BigDecimal.valueOf(govScore));
        score.setTotalScore(BigDecimal.valueOf(totalScore));
        score.setGrade(grade);
        score.setCalculatedAt(Instant.now());

        return scoreRepository.save(score);
    }

    public SustainabilityScore getLatestScore(Long companyId) {
        return scoreRepository.findTopByCompanyIdOrderByCalculatedAtDesc(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("No score found for company id: " + companyId));
    }

    public List<SustainabilityScore> getScoreHistory(Long companyId) {
        return scoreRepository.findByCompanyIdOrderByPeriodStartDesc(companyId);
    }

    // Environment Score (40%)
    private double calculateEnvironmentScore(Long companyId, LocalDate start, LocalDate end) {
        double co2Score = calculateCo2Score(companyId, start, end);
        double energyScore = calculateEnergyScore(companyId, start, end);
        double waterScore = calculateWaterScore(companyId, start, end);
        double wasteScore = calculateWasteScore(companyId, start, end);

        return (co2Score * 0.40) + (energyScore * 0.30) + (waterScore * 0.15) + (wasteScore * 0.15);
    }

    private double calculateCo2Score(Long companyId, LocalDate start, LocalDate end) {
        BigDecimal totalCo2 = emissionRepository.getTotalCo2(companyId, start, end);
        BigDecimal targetCo2 = companyRepository.getCo2Target(companyId);

        if (targetCo2 == null || totalCo2 == null || totalCo2.compareTo(BigDecimal.ZERO) <= 0) return 80.0;
        if (totalCo2.compareTo(targetCo2) <= 0) return 100.0;

        return (targetCo2.doubleValue() / totalCo2.doubleValue()) * 100;
    }

    private double calculateEnergyScore(Long companyId, LocalDate start, LocalDate end) {
        BigDecimal total = energyRepository.getTotalKwh(companyId, start, end);
        BigDecimal renewable = energyRepository.getTotalRenewableKwh(companyId, start, end);

        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) return 70.0;
        return Math.min(100.0, renewable.doubleValue() / total.doubleValue() * 100);
    }

    private double calculateWaterScore(Long companyId, LocalDate start, LocalDate end) {
        BigDecimal consumed = waterRepository.getTotalConsumedLiters(companyId, start, end);
        BigDecimal recycled = waterRepository.getTotalRecycledLiters(companyId, start, end);

        if (consumed == null || consumed.compareTo(BigDecimal.ZERO) == 0) return 70.0;
        return Math.min(100.0, recycled.doubleValue() / consumed.doubleValue() * 100);
    }

    private double calculateWasteScore(Long companyId, LocalDate start, LocalDate end) {
        BigDecimal total = wasteRepository.getTotalKg(companyId, start, end);
        BigDecimal recycled = wasteRepository.getTotalRecycledKg(companyId, start, end);

        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) return 70.0;
        return Math.min(100.0, recycled.doubleValue() / total.doubleValue() * 100);
    }

    private double calculateSocialScore(Long companyId, LocalDate start, LocalDate end) {
        int incidents = socialRepository.getTotalSafetyIncidents(companyId, start, end);
        double safetyScore = incidents == 0 ? 100.0 : incidents <= 2 ? 80.0 : incidents <= 5 ? 60.0 : 20.0;

        double femaleRatio = socialRepository.getAverageFemaleRatio(companyId, start, end);
        double diversityScore = Math.min(100.0, femaleRatio * 2);

        BigDecimal avgTraining = socialRepository.getAverageTrainingHours(companyId, start, end);
        double trainingScore = Math.min(100.0, (avgTraining != null ? avgTraining.doubleValue() : 0) / 40.0 * 100);

        BigDecimal avgSatisfaction = socialRepository.getAverageSatisfactionScore(companyId, start, end);
        double satisfactionScore = avgSatisfaction != null ? avgSatisfaction.doubleValue() : 70.0;

        return (safetyScore * 0.40) + (diversityScore * 0.20) +
                (trainingScore * 0.20) + (satisfactionScore * 0.20);
    }

    private double calculateGovernanceScore(Long companyId, LocalDate start, LocalDate end) {
        BigDecimal compliance = governanceRepository.getAverageComplianceScore(companyId, start, end);
        int violations = governanceRepository.getTotalViolations(companyId, start, end);
        boolean ethicsDone = governanceRepository.hasEthicsTraining(companyId, start, end);

        double score = compliance != null ? compliance.doubleValue() : 70.0;
        score = score - (violations * 5);
        if (ethicsDone) score += 10;

        return Math.max(0.0, Math.min(100.0, score));
    }

    public String determineGrade(double score) {
        if (score >= 90) return "A";
        if (score >= 75) return "B";
        if (score >= 60) return "C";
        if (score >= 45) return "D";
        return "F";
    }
}