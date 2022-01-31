package com.unina.springnatour.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostDto implements Serializable {
    private Long id;
    private String description;
    private Boolean reported = Boolean.FALSE;
    private Long userId;
    private Long routeId;
    private List<PostPhotoDto> photos;
}
