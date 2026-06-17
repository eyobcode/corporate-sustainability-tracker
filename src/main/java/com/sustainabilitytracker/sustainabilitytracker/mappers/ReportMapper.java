package com.sustainabilitytracker.sustainabilitytracker.mappers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.response.ReportResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.EsgReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "scoreId", source = "score.id")
    @Mapping(target = "downloadUrl", expression = "java('/api/reports/' + report.getId() + '/download')")
    ReportResponse toResponse(EsgReport report);

    List<ReportResponse> toResponseList(List<EsgReport> reports);
}
