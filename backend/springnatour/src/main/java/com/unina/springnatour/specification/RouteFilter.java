package com.unina.springnatour.specification;

import lombok.Data;

@Data
public class RouteFilter {
    private String title;
    private Integer avgDifficulty;
    private Float avgDuration;
    private Boolean disabilitySafe;
    private Double longitude;
    private Double latitude;
}
