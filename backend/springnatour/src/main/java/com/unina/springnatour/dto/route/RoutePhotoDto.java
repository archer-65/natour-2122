package com.unina.springnatour.dto.route;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoutePhotoDto implements Serializable {
    private Long id;
    private String photoUrl;
}
