package com.unina.springnatour.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unina.springnatour.model.User;
import com.unina.springnatour.model.route.Route;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReportDto implements Serializable {
    @JsonProperty("report_id")
    private Long reportId;

    @JsonProperty("report_title")
    private String reportTitle;

    @JsonProperty("report_description")
    private String reportDescription;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("reported_route_id")
    private Long reportedRouteId;
}