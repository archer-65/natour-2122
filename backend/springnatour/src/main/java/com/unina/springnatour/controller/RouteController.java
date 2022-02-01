package com.unina.springnatour.controller;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/routes")
    public ResponseEntity<List<RouteDto>> getAllRoutes() {

        List<RouteDto> routeDtoList = routeService.getAllRoutes();

        if (!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }
}
