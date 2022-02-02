package com.unina.springnatour.dto.rating;


import lombok.Data;

@Data
public class RatingDto {
    private Long  id;
    private Integer difficulty;
    private Float duration;
    private Long userId;
    private Long routeId;
}
