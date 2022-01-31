package com.unina.springnatour.dto;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MapperConfig;

import java.util.List;

@MapperConfig
public interface BaseMapper<Source, Target> {

    Target toDto(Source source);

    @InheritInverseConfiguration(name = "toDto")
    Source toEntity(Target target);

    @InheritConfiguration(name = "toDto")
    List<Target> toDto(List<Source> sources);

    @InheritConfiguration(name = "toEntity")
    List<Source> toEntity(List<Target> targets);
}
