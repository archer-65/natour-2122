package com.unina.springnatour.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RouteTitleDto implements Serializable {
    @JsonProperty("route_id")
    private Long routeId;

    @JsonProperty("route_title")
    private String routeTitle;
}
