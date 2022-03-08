package com.unina.springnatour.dto.compilation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CompilationDto {
    @JsonProperty("compilation_id")
    private Long compilationId;

    @JsonProperty("compilation_title")
    private String compilationTitle;

    @JsonProperty("compilation_description")
    private String compilationDescription;

    @JsonProperty("compilation_creation_date")
    private LocalDateTime compilationCreationDate;

    @JsonProperty("compilation_photo")
    private String compilationPhoto;

    @JsonProperty("compilation_author")
    private UserDto author;
}
