package com.unina.springnatour.dto.route;

import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RouteDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Integer avgDifficulty;
    private Float avgDuration;
    private Boolean disabledFriendly = false;
    private LocalDateTime creationDate;
    private LocalDateTime modifiedDate;
    private List<RoutePhotoDto> photos;
    private List<RouteStopDto> stops;
    private UserDto user;
}
