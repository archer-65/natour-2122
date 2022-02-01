package com.unina.springnatour.dto.route;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class RouteDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Integer avgDifficulty;
    private Float avgDuration;
    private Boolean disabilitySafe;
    private LocalDate modifiedDate;
    private List<RoutePhotoDto> photos;
    private List<RouteStopDto> stops;
    private Long userId;
}
