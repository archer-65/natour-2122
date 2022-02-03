package com.unina.springnatour.specification;

import lombok.Data;

import java.io.Serializable;

/**
 * Object containing fields for Specification API Query.
 * Each field represents a filter and could be null
 */
@Data
public class RouteFilter implements Serializable {
    private String title;
    private Integer avgDifficulty;
    private Float avgDuration;
    private Boolean disabledFriendly;
    private Double longitude;
    private Double latitude;
}
