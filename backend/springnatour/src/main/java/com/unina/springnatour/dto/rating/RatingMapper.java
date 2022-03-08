package com.unina.springnatour.dto.rating;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.dto.report.ReportDto;
import com.unina.springnatour.model.Rating;
import com.unina.springnatour.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingMapper extends BaseMapper<Rating, RatingDto> {

    @Override
    @Mappings({
            @Mapping(target = "ratingId", source = "id"),
            @Mapping(target = "ratingDifficulty", source = "difficulty"),
            @Mapping(target = "ratingDuration", source = "duration"),
            @Mapping(target = "authorId", source = "user.id"),
            @Mapping(target = "ratedRouteId", source = "route.id") ,
    })
    RatingDto toDto(Rating rating);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "ratingId"),
            @Mapping(target = "difficulty", source = "ratingDifficulty"),
            @Mapping(target = "duration", source = "ratingDuration"),
            @Mapping(target = "user.id", source = "authorId"),
            @Mapping(target = "route.id", source = "ratedRouteId") ,
    })
    Rating toEntity(RatingDto ratingDto);
}
