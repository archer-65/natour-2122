package com.unina.springnatour.controller;

import com.unina.springnatour.model.maps.MapsResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DirectionsController {

    @Value("${api.key}")
    private String apiKey;

    @Value("${base.url}")
    private String baseUrl;

    @GetMapping("/directions")
    public ResponseEntity<MapsResponse> getDirections(
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination,
            @RequestParam(name = "mode", defaultValue = "walking") String mode,
            @RequestParam(name = "waypoints", defaultValue = "") String waypoints
    ) {
        String url = baseUrl + "?"
                + "origin=" + origin
                + "&destination=" + destination
                + "&waypoints=" + waypoints
                + "&mode=" + mode
                + "&key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        MapsResponse response = restTemplate.getForObject(url, MapsResponse.class);

        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
