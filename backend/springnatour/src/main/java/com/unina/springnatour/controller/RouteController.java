package com.unina.springnatour.controller;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.route.RouteTitleDto;
import com.unina.springnatour.model.route.Route;
import com.unina.springnatour.service.RouteService;
import com.unina.springnatour.specification.RouteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * Gets a route
     *
     * @param id the identifier of the route
     * @return RouteDTO
     */
    @GetMapping("/routes/{id}")
    public ResponseEntity<RouteDto> getRouteById(@PathVariable Long id) {

        RouteDto routeDto = routeService.getRouteById(id);

        return new ResponseEntity<>(routeDto, HttpStatus.OK);
    }

    /**
     * Gets all the routes
     *
     * @return List of RouteDTO
     */
    @GetMapping("/routes/all")
    public ResponseEntity<List<RouteDto>> getAllRoutes() {

        List<RouteDto> routeDtoList = routeService.getAllRoutes();

        if (!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     */
    @GetMapping("/routes")
    public ResponseEntity<List<RouteDto>> getAllRoutes(
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        List<RouteDto> routeDtoList = routeService.getAllRoutes(pageNo, pageSize);

        if (!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all the routes for a certain user
     *
     * @param userId the identifier of the user
     * @return List of RouteDTO Objects with HTTP Status OK if the list is not empty
     */
    @GetMapping("/routes/search")
    public ResponseEntity<List<RouteDto>> getAllRoutesByUserId(@RequestParam(name = "user_id") Long userId) {

        List<RouteDto> routeDtoList = routeService.getAllRoutesByUserId(userId);

        if (!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all the routes for a certain user
     *
     * @param userId the identifier of the user
     * @return List of RouteDTO Objects with HTTP Status OK if the list is not empty
     */
    @GetMapping("/routes/search_page")
    public ResponseEntity<List<RouteDto>> getAllRoutesByUserId(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        List<RouteDto> routeDtoList = routeService.getAllRoutesByUserId(userId, pageNo, pageSize);

        if (!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Searches all routes by filter
     *
     * @return List of RouteDTO Objects with HTTP Status OK if the list is not empty
     * @see RouteFilter
     * @see com.unina.springnatour.specification.RouteSpecifications
     */
    @GetMapping("/routes/filter")
    public ResponseEntity<List<RouteDto>> getAllRoutesByFilter(
            @RequestParam("query") String query,
            @RequestParam(value = "difficulty", required = false) Integer difficulty,
            @RequestParam(value = "duration", required = false) Float duration,
            @RequestParam(value = "disability_friendly", required = false) Boolean isDisabilityFriendly,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "distance", required = false) Float distance,
            @RequestParam(name = "page_no", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        RouteFilter filter = new RouteFilter(query, difficulty, duration, isDisabilityFriendly, latitude, longitude, distance);

        List<RouteDto> routeDtoList = routeService.getAllRoutesByFilter(filter, pageNo, pageSize);

        if (!routeDtoList.isEmpty()) {
            return new ResponseEntity<>(routeDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/routes/search_title")
    public ResponseEntity<List<RouteTitleDto>> getRoutesTitleByQuery(@RequestParam String query) {

        List<RouteTitleDto> routeTitleDtoList = routeService.getRoutesTitleByQuery(query);

        if (!routeTitleDtoList.isEmpty()) {
            return new ResponseEntity<>(routeTitleDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new route
     *
     * @param routeDto the RouteDTO Object containing the required fields
     * @return HTTP Status CREATED after insertion
     */
    @PostMapping("/routes/add")
    public ResponseEntity<?> addRoute(@RequestBody RouteDto routeDto) {

        Route createdRoute = routeService.addRoute(routeDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRoute.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    /**
     * Updates an existing route
     *
     * @param id       the identifier of the route
     * @param routeDto the RouteDTO Object with the updated route
     * @return HTTP Status CREATED after update
     */
    @PutMapping("/routes/{id}/update")
    public ResponseEntity<?> updateRoute(@PathVariable Long id,
                                         @RequestBody RouteDto routeDto) {

        routeService.updateRoute(id, routeDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes an existing route
     *
     * @param id the identifier of the route
     * @return HTTP Status OK
     */
    @DeleteMapping("/routes/{id}/delete")
    public ResponseEntity<?> deleteRoute(@PathVariable Long id) {

        routeService.deleteRoute(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}