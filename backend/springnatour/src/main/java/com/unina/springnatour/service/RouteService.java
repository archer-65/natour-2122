package com.unina.springnatour.service;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.route.RouteMapper;
import com.unina.springnatour.exception.RouteNotFoundException;
import com.unina.springnatour.exception.UserNotFoundException;
import com.unina.springnatour.model.route.Route;
import com.unina.springnatour.model.route.RouteStop;
import com.unina.springnatour.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RouteMapper routeMapper;

    /**
     * Get a route
     * @param id the identifier of the route
     * @return RouteDTO Object, mapped from Entity, or throw Exception
     */
    public RouteDto getRouteById(Long id) {
        return routeMapper.toDto(routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException(id)));
    }

    /**
     * Get all routes
     * @return List of RouteDTO Objects, mapped from Entity
     */
    public List<RouteDto> getAllRoutes() {
        return routeMapper.toDto(routeRepository.findAll()
                .stream()
                .toList());
    }

    /**
     * Add a route, also set Route association for every Stop in the List (required to persist)
     * @param routeDto RouteDTO Object with required fields
     */
    public void addRoute(RouteDto routeDto) {
        Route route = routeMapper.toEntity(routeDto);

//        for(RouteStop stop : route.getStops()) {
//            stop.setRoute(route);
//        }

        routeRepository.save(route);
    }
}
