package com.unina.springnatour.dto.route;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;
import org.mapstruct.Mappings;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RouteDto implements Serializable {
    @JsonProperty("route_id")
    private Long routeId;

    @JsonProperty("route_title")
    private String routeTitle;

    @JsonProperty("route_description")
    private String routeDescription;

    @JsonProperty("route_difficulty")
    private Integer routeDifficulty;

    @JsonProperty("route_duration")
    private Float routeDuration;

    @JsonProperty("is_disability_friendly")
    private Boolean isDisabilityFriendly = false;

    @JsonProperty("route_creation_date")
    private LocalDateTime routeCreationDate;

    @JsonProperty("route_modified_date")
    private LocalDateTime routeModifiedDate;

    @JsonProperty("is_route_reported")
    private Boolean isRouteReported;

    @JsonProperty("route_photos")
    private List<RoutePhotoDto> routePhotos;

    @JsonProperty("route_stops")
    private List<RouteStopDto> routeStops;

    @JsonProperty("route_author")
    private UserDto author;
}
