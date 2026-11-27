package tn.esprit.ecocityservice.ecocity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.ecocityservice.ecocity.dto.EnvReportRequest;
import tn.esprit.ecocityservice.ecocity.dto.EnvReportResponse;
import tn.esprit.ecocityservice.ecocity.entity.EnvReport;
import tn.esprit.ecocityservice.ecocity.entity.Zone;

@Mapper(componentModel = "spring")
public interface EnvReportMapper {

    @Mapping(target = "zoneId", source = "zone.id")
    @Mapping(target = "zoneName", source = "zone.name")
    EnvReportResponse toResponse(EnvReport report);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "zone", source = "zone")
    @Mapping(target = "periodStart", source = "request.periodStart")
    @Mapping(target = "periodEnd", source = "request.periodEnd")
    @Mapping(target = "avgValue", source = "request.avgValue")
    @Mapping(target = "maxValue", source = "request.maxValue")
    void updateEntity(EnvReportRequest request, Zone zone, @MappingTarget EnvReport report);
}
