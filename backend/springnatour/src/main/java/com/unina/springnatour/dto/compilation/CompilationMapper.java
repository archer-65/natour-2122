package com.unina.springnatour.dto.compilation;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.unina.springnatour.dto.BaseMapper;
import com.unina.springnatour.model.Compilation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompilationMapper extends BaseMapper<Compilation, CompilationDto> {

    @Override
    @Mapping(target = "userId", source = "user.id")
    CompilationDto toDto(Compilation compilation);

    @Override
    @Mapping(target = "user.id", source = "userId")
    Compilation toEntity(CompilationDto compilationDto);
}
