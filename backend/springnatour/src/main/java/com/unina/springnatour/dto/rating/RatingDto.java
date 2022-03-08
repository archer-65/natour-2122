package com.unina.springnatour.dto.rating;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RatingDto {
    @JsonProperty("rating_id")
    private Long ratingId;

    @JsonProperty("rating_difficulty")
    private Integer ratingDifficulty;

    @JsonProperty("rating_duration")
    private Float ratingDuration;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("rated_route_id")
    private Long ratedRouteId;
}
