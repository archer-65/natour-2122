package com.unina.springnatour.model.maps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapsRoute implements Serializable {
    @JsonProperty("overview_polyline")
    private MapsOverviewPolyline overviewPolyline;
}
