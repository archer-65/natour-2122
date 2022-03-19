package com.unina.springnatour.dto.report;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.dto.route.RouteTitleDto;
import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.model.Report;
import com.unina.springnatour.model.User;
import com.unina.springnatour.model.route.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper extends BaseMapper<Report, ReportDto> {

    @Override
    @Mappings({
            @Mapping(target = "reportId", source = "id"),
            @Mapping(target = "reportTitle", source = "title"),
            @Mapping(target = "reportDescription", source = "description"),
            @Mapping(target = "reportedRoute", source = "route"),
            @Mapping(target = "author", source = "user")
    })
    ReportDto toDto(Report report);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "reportId"),
            @Mapping(target = "title", source = "reportTitle"),
            @Mapping(target = "description", source = "reportDescription"),
            @Mapping(target = "route", source = "reportedRoute"),
            @Mapping(target = "user", source = "author")
    })
    Report toEntity(ReportDto reportDto);

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

    @Mapping(target = "routeId", source = "id")
    @Mapping(target = "routeTitle", source = "title")
    RouteTitleDto toDto(Route route);

    @Mapping(target = "id", source = "routeId")
    @Mapping(target = "title", source = "routeTitle")
    Route toEntity(RouteTitleDto routeTitleDto);
}
