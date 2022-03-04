package com.unina.springnatour.dto.route;

import lombok.Data;

import java.io.Serializable;

@Data
public class RouteTitleDto implements Serializable {
    private Long id;
    private String title;
}
