package com.unina.springnatour.dto.route;

import lombok.Data;

import java.io.Serializable;

@Data
public class RouteStopDto implements Serializable {
    private Long Id;
    private Integer stopNumber;
    private Double longitude;
    private Double latitude;
}
