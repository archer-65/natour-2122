package com.unina.springnatour.dto.post;

import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto implements Serializable {
    private Long id;
    private String description;
    private Boolean reported = Boolean.FALSE;
    private LocalDateTime creationDate;
    private List<PostPhotoDto> photos;
    private UserDto user;
    private Long routeId;
    private String routeTitle;
}
