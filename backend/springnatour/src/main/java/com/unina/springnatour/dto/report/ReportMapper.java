package com.unina.springnatour.dto.report;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.Report;
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
            @Mapping(target = "authorId", source = "user.id"),
            @Mapping(target = "reportedRouteId", source = "route.id") ,
    })
    ReportDto toDto(Report report);

    @Override
    @Mappings({
            @Mapping(target = "id", source = "reportId"),
            @Mapping(target = "title", source = "reportTitle"),
            @Mapping(target = "description", source = "reportDescription"),
            @Mapping(target = "user.id", source = "authorId"),
            @Mapping(target = "route.id", source = "reportedRouteId"),
    })
    Report toEntity(ReportDto reportDto);
}
