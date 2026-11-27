package tn.esprit.ecocityservice.ecocity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tn.esprit.ecocityservice.ecocity.dto.ZoneRequest;
import tn.esprit.ecocityservice.ecocity.dto.ZoneResponse;
import tn.esprit.ecocityservice.ecocity.entity.Zone;

@Mapper(componentModel = "spring", imports = MapperHelper.class)
public interface ZoneMapper {

    @Mapping(target = "boundary", expression = "java(MapperHelper.toWkt(zone.getBoundary()))")
    ZoneResponse toResponse(Zone zone);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(request.name().trim())")
    @Mapping(target = "boundary", expression = "java(MapperHelper.toPolygon(request.boundary()))")
    void updateEntity(ZoneRequest request, @MappingTarget Zone zone);
}
