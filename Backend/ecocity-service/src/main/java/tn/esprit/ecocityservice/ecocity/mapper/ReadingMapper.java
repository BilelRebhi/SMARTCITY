package tn.esprit.ecocityservice.ecocity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.ecocityservice.ecocity.dto.ReadingResponse;
import tn.esprit.ecocityservice.ecocity.entity.Measurement;

@Mapper(componentModel = "spring")
public interface ReadingMapper {

    @Mapping(target = "stationId", source = "station.id")
    @Mapping(target = "stationCode", source = "station.code")
    ReadingResponse toResponse(Measurement measurement);
}
