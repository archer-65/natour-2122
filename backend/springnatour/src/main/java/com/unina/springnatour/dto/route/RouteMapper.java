package com.unina.springnatour.dto.route;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.route.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteMapper extends BaseMapper<Route, RouteDto> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "stops", source = "stops")
    @Mapping(target = "photos", source = "photos")
    RouteDto toDto(Route route);

    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "stops", source = "stops")
    @Mapping(target = "photos", source = "photos")
    Route toEntity(RouteDto routeDto);
}
