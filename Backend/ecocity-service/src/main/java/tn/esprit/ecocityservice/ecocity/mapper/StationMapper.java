package tn.esprit.ecocityservice.ecocity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tn.esprit.ecocityservice.ecocity.dto.StationRequest;
import tn.esprit.ecocityservice.ecocity.dto.StationResponse;
import tn.esprit.ecocityservice.ecocity.entity.MonitoringStation;
import tn.esprit.ecocityservice.ecocity.entity.Zone;

@Mapper(componentModel = "spring")
public interface StationMapper {

    @Mapping(target = "zoneId", source = "zone.id")
    @Mapping(target = "zoneName", source = "zone.name")
    StationResponse toResponse(MonitoringStation station);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", expression = "java(request.code().trim())")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "latitude", source = "request.latitude")
    @Mapping(target = "longitude", source = "request.longitude")
    // status and sampling interval keep their current value (or the default) when not sent
    @Mapping(target = "status", source = "request.status",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "samplingIntervalSec", source = "request.samplingIntervalSec",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "zone", source = "zone")
    void updateEntity(StationRequest request, Zone zone, @MappingTarget MonitoringStation station);
}
