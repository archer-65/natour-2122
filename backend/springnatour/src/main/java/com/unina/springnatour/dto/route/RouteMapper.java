package com.unina.springnatour.dto.route;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.route.Route;
import com.unina.springnatour.model.route.RoutePhoto;
import com.unina.springnatour.model.route.RouteStop;
import org.hibernate.annotations.Parent;
import org.mapstruct.*;

import java.util.List;

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

    @AfterMapping
    default void mapBidirectional(@MappingTarget Route route){

        List<RouteStop> stops = route.getStops();
        if (stops != null) {
            stops.forEach(stop -> stop.setRoute(route));
        }

        List<RoutePhoto> photos = route.getPhotos();
        if (photos != null) {
            photos.forEach(photo -> photo.setRoute(route));
        }
    }
}
