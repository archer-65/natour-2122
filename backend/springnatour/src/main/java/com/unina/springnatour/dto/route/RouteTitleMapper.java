package com.unina.springnatour.dto.route;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.route.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteTitleMapper extends BaseMapper<Route, RouteTitleDto> {

    @Override
    @Mapping(target = "routeId", source = "id")
    @Mapping(target = "routeTitle", source = "title")
    RouteTitleDto toDto(Route route);

    @Override
    @Mapping(target = "id", source = "routeId")
    @Mapping(target = "title", source = "routeTitle")
    Route toEntity(RouteTitleDto routeTitleDto);
}
