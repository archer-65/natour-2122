package com.unina.springnatour.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unina.springnatour.dto.route.RouteTitleDto;
import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto implements Serializable {
    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("post_description")
    private String postDescription;

    @JsonProperty("is_post_reported")
    private Boolean isPostReported = Boolean.FALSE;

    @JsonProperty("creation_date")
    private LocalDateTime postCreationDate;

    @JsonProperty("post_photos")
    private List<PostPhotoDto> postPhotos;

    @JsonProperty("post_author")
    private UserDto postAuthor;

    @JsonProperty("tagged_route")
    private RouteTitleDto taggedRoute;
}
