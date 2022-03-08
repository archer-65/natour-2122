package com.unina.springnatour.dto.route;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.model.User;
import com.unina.springnatour.model.route.Route;
import com.unina.springnatour.model.route.RoutePhoto;
import com.unina.springnatour.model.route.RouteStop;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RouteMapper extends BaseMapper<Route, RouteDto> {

    @Override
    @Mappings({
            @Mapping(target = "routeId", source = "id"),
            @Mapping(target = "routeTitle", source = "title"),
            @Mapping(target = "routeDescription", source = "description"),
            @Mapping(target = "routeDifficulty", source = "avgDifficulty"),
            @Mapping(target = "routeDuration", source = "avgDuration"),
            @Mapping(target = "isDisabilityFriendly", source = "disabledFriendly"),
            @Mapping(target = "routeCreationDate", source = "creationDate"),
            @Mapping(target = "routeModifiedDate", source = "modifiedDate"),
            @Mapping(target = "isRouteReported", source = "isReported"),
            @Mapping(target = "routePhotos", source = "photos"),
            @Mapping(target = "routeStops", source = "stops"),
            @Mapping(target = "author", source = "user")
    })
    RouteDto toDto(Route route);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "routeId"),
            @Mapping(target = "title", source = "routeTitle"),
            @Mapping(target = "description", source = "routeDescription"),
            @Mapping(target = "avgDifficulty", source = "routeDifficulty"),
            @Mapping(target = "avgDuration", source = "routeDuration"),
            @Mapping(target = "disabledFriendly", source = "isDisabilityFriendly"),
            @Mapping(target = "creationDate", source = "routeCreationDate"),
            @Mapping(target = "modifiedDate", source = "routeModifiedDate"),
            @Mapping(target = "isReported", source = "isRouteReported"),
            @Mapping(target = "photos", source = "routePhotos"),
            @Mapping(target = "stops", source = "routeStops"),
            @Mapping(target = "user", source = "author")
    })
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

    @Mappings({
            @Mapping(target = "stopId", source = "id"),
            @Mapping(target = "stopNumber", source = "stopNumber"),
            @Mapping(target = "stopLatitude", source = "latitude"),
            @Mapping(target = "stopLongitude", source = "longitude"),
    })
    RouteStopDto toDto(RouteStop routeStop);

    @Mappings({
            @Mapping(target = "id", source = "stopId"),
            @Mapping(target = "stopNumber", source = "stopNumber"),
            @Mapping(target = "latitude", source = "stopLatitude"),
            @Mapping(target = "longitude", source = "stopLongitude"),
    })
    RouteStop toEntity(RouteStopDto routeStopDto);

    @Mappings({
            @Mapping(target = "userId", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "profilePhoto", source = "photo"),
    })
    UserDto toDto(User user);

    @Mappings({
            @Mapping(target = "id", source = "userId"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "photo", source = "profilePhoto"),
    })
    User toEntity(UserDto userDto);
}
