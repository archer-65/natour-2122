package com.unina.springnatour.dto.report;

import com.unina.springnatour.model.User;
import com.unina.springnatour.model.route.Route;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReportDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private Long routeId;
}