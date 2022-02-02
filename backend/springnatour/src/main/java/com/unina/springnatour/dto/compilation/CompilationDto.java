package com.unina.springnatour.dto.compilation;

import com.unina.springnatour.dto.route.RouteDto;
import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private String title;
    private String description;
    private String photoUrl;
    private Long userId;
    private List<RouteDto> routes;
}
