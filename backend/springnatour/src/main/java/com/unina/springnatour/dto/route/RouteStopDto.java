package com.unina.springnatour.dto.route;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.mapstruct.Mapping;

import java.io.Serializable;

@Data
public class RouteStopDto implements Serializable {
    @JsonProperty("stop_id")
    private Long stopId;

    @JsonProperty("stop_number")
    private Integer stopNumber;

    @JsonProperty("stop_latitude")
    private Double stopLatitude;

    @JsonProperty("stop_longitude")
    private Double stopLongitude;
}
