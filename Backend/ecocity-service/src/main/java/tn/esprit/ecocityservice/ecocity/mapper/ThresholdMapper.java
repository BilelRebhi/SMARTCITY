package tn.esprit.ecocityservice.ecocity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.ecocityservice.ecocity.dto.ThresholdRequest;
import tn.esprit.ecocityservice.ecocity.dto.ThresholdResponse;
import tn.esprit.ecocityservice.ecocity.entity.Threshold;

@Mapper(componentModel = "spring", imports = MapperHelper.class)
public interface ThresholdMapper {

    ThresholdResponse toResponse(Threshold threshold);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unit", expression = "java(MapperHelper.resolveUnit(request.indicator(), request.unit()))")
    @Mapping(target = "active", expression = "java(request.active() == null || request.active())")
    void updateEntity(ThresholdRequest request, @MappingTarget Threshold threshold);
}
