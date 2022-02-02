package com.unina.springnatour.dto.rating;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.dto.report.ReportDto;
import com.unina.springnatour.model.Rating;
import com.unina.springnatour.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingMapper extends BaseMapper<Rating, RatingDto> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "routeId", source = "route.id")
    RatingDto toDto(Rating rating);

    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "route.id", source = "routeId")
    Rating toEntity(RatingDto ratingDto);
}
