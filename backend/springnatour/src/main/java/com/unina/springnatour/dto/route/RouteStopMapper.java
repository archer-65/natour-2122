package com.unina.springnatour.dto.route;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.route.RouteStop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteStopMapper extends BaseMapper<RouteStop, RouteStopDto> {

    @Override
    @Mappings({
            @Mapping(target = "stopId", source = "id"),
            @Mapping(target = "stopNumber", source = "stopNumber"),
            @Mapping(target = "stopLatitude", source = "latitude"),
            @Mapping(target = "stopLongitude", source = "longitude"),
    })
    RouteStopDto toDto(RouteStop routeStop);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "stopId"),
            @Mapping(target = "stopNumber", source = "stopNumber"),
            @Mapping(target = "latitude", source = "stopLatitude"),
            @Mapping(target = "longitude", source = "stopLongitude"),
    })
    RouteStop toEntity(RouteStopDto routeStopDto);
}
