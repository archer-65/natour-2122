package com.unina.springnatour.controller;

import com.unina.springnatour.dto.route.RouteDto;
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all the routes for a certain user
     * @param userId the identifier of the user
     * @return List of RouteDTO Objects with HTTP Status OK if the list is not empty
     */
    @GetMapping("/routes/search")
    public ResponseEntity<List<RouteDto>> getAllRoutesByUserId(@RequestParam Long userId) {

        List<RouteDto> routeDtoList = routeService.getAllRoutesByUserId(userId);

        if (!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/routes/filter")
    public ResponseEntity<List<RouteDto>> getAllRoutesByFilter(@RequestBody RouteDto routeDto) {

        List<RouteDto> routeDtoList = routeService.getAllRoutesByFilter(routeDto);

        if(!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new route
     * @param routeDto the RouteDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/routes/add")
    public ResponseEntity<?> addRoute(@RequestBody RouteDto routeDto) {

        routeService.addRoute(routeDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Update an existing route
     * @param id the identifier of the route
     * @param routeDto the RouteDTO Object with updated values
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/routes/{id}/update")
    public ResponseEntity<?> updateRoute(@PathVariable Long id,
                                         @RequestBody RouteDto routeDto) {

        routeService.updateRoute(id, routeDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete a route
     * @param id the identifier of the route
     * @return HTTP Status OK
     */
    @DeleteMapping("/routes/{id}/delete")
    public ResponseEntity<?> deleteRoute(@PathVariable Long id) {

        routeService.deleteRoute(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}