package com.unina.springnatour.dto.post;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.user.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostDto implements Serializable {
    private Long id;
    private String description;
    private Boolean reported = Boolean.FALSE;
    private List<PostPhotoDto> photos;
    private UserDto user;
    private Long routeId;
    private String routeTitle;
}
