package com.unina.springnatour.dto.route;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.route.RouteStop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteStopMapper extends BaseMapper<RouteStop, RouteStopDto> {

    @Override
    @Mapping(target = "longitude", source = "location.longitude")
    @Mapping(target = "latitude", source = "location.latitude")
    RouteStopDto toDto(RouteStop routeStop);

    @Override
    @Mapping(target = "location.longitude", source = "longitude")
    @Mapping(target = "location.latitude", source = "latitude")
    RouteStop toEntity(RouteStopDto routeStopDto);
}
