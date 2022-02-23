package com.unina.springnatour.dto.compilation;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private String photoUrl;
    private UserDto user;
  //  private List<RouteDto> routes;
}
