package tn.esprit.ecocityservice.ecocity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.ecocityservice.ecocity.dto.AlertResponse;
import tn.esprit.ecocityservice.ecocity.entity.PollutionAlert;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    @Mapping(target = "thresholdValue", source = "threshold.maxValue")
    @Mapping(target = "measurementId", source = "measurement.id")
    @Mapping(target = "thresholdId", source = "threshold.id")
    @Mapping(target = "stationId", source = "measurement.station.id")
    @Mapping(target = "zoneId", source = "measurement.station.zone.id")
    AlertResponse toResponse(PollutionAlert alert);
}
