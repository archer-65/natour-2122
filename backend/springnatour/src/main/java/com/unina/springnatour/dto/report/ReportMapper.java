package com.unina.springnatour.dto.report;

import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper extends BaseMapper<Report, ReportDto> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "routeId", source = "route.id")
    ReportDto toDto(Report report);

    @Override
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "route.id", source = "routeId")
    Report toEntity(ReportDto reportDto);
}
