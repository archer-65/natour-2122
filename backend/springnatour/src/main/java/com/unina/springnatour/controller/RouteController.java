package com.unina.springnatour.controller;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.user.UserDto;
import com.unina.springnatour.model.route.Route;
import com.unina.springnatour.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * Get a route
     * @param id the identifier of the route
     * @return RouteDTO
     */
    @GetMapping("/routes/{id}")
    public ResponseEntity<RouteDto> getRouteById(@PathVariable Long id) {

        RouteDto routeDto = routeService.getRouteById(id);

        return new ResponseEntity<>(routeDto, HttpStatus.OK);
    }

    /**
     * Get all the routes
     * @return List of RouteDTO
     */
    @GetMapping("/routes")
    public ResponseEntity<List<RouteDto>> getAllRoutes() {

        List<RouteDto> routeDtoList = routeService.getAllRoutes();

        if (!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new route
     * @param routeDto the RouteDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/routes")
    public ResponseEntity<?> addRoute(@RequestBody RouteDto routeDto) {

        routeService.addRoute(routeDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
