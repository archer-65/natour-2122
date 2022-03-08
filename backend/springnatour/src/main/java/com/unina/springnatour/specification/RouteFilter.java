package com.unina.springnatour.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Object containing fields for Specification API Query.
 * Each field represents a filter and could be null
 */
@Data
@AllArgsConstructor
public class RouteFilter implements Serializable {
    private String title;
    private Integer avgDifficulty;
    private Float avgDuration;
    private Boolean disabledFriendly;
    private Double latitude;
    private Double longitude;
    private Float distance;
}
