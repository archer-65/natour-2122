package com.unina.springnatour.dto.route;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.route.Route;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteTitleMapper extends BaseMapper<Route, RouteTitleDto> {

    @Override
    RouteTitleDto toDto(Route route);

    @Override
    Route toEntity(RouteTitleDto routeTitleDto);
}
