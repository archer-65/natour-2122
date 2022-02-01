package com.unina.springnatour.dto.route;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.route.RoutePhoto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoutePhotoMapper extends BaseMapper<RoutePhoto, RoutePhotoDto> {

    @Override
    RoutePhotoDto toDto(RoutePhoto routePhoto);

    @Override
    RoutePhoto toEntity(RoutePhotoDto routePhotoDto);
}
