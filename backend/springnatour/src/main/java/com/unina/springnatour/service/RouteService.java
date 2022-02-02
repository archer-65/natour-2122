package com.unina.springnatour.service;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.route.RouteMapper;
import com.unina.springnatour.exception.RouteNotFoundException;
import com.unina.springnatour.model.route.Route;
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
     *
     * @param id the identifier of the route
     * @return RouteDTO Object, mapped from Entity, or throw Exception
     */
    public RouteDto getRouteById(Long id) {
        return routeMapper.toDto(routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException(id)));
    }

    /**
     * Get all routes
     *
     * @return List of RouteDTO Objects, mapped from Entity
     */
    public List<RouteDto> getAllRoutes() {
        return routeMapper.toDto(routeRepository.findAll()
                .stream()
                .toList());
    }

    public List<RouteDto> getAllRoutesByUserId(Long userId) {
        return routeMapper.toDto(routeRepository.findAll()
                .stream()
                .toList());
    }

    /**
     * Add a route
     * @param routeDto RouteDTO Object with required fields
     */
    public void addRoute(RouteDto routeDto) {
        Route route = routeMapper.toEntity(routeDto);

        routeRepository.save(route);
    }

    /**
     * Update a route
     * @param id the identifier of the route
     * @param routeDto RouteDTO Object updated
     */
    public void updateRoute(Long id, RouteDto routeDto) {
        routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException(id));

        routeRepository.save(routeMapper.toEntity(routeDto));
    }

    /**
     * Delete a route
     * @param id the identifier of the route
     */
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}