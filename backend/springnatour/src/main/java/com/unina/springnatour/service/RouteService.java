package com.unina.springnatour.service;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.route.RouteMapper;
import com.unina.springnatour.exception.RouteNotFoundException;
import com.unina.springnatour.model.route.Route;
import com.unina.springnatour.repository.RouteRepository;
import com.unina.springnatour.specification.RouteFilter;
import com.unina.springnatour.specification.RouteSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private RouteMapper routeMapper;

    /**
     * Gets a route
     *
     * @param id the identifier of the route
     * @return RouteDTO Object, mapped from Entity, or throws Exception
     */
    public RouteDto getRouteById(Long id) {
        return routeMapper.toDto(routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException(id)));
    }

    /**
     * Gets all routes
     * @return List of RouteDTO Objects, mapped from Entity
     */
    public List<RouteDto> getAllRoutes() {
        return routeMapper.toDto(routeRepository.findAll()
                .stream()
                .toList());
    }

    public List<RouteDto> getAllRoutes(Integer pageNo, Integer pageSize) {
        return routeMapper.toDto(routeRepository.findAll(PageRequest.of(pageNo, pageSize))
                .stream()
                .toList());
    }

    public List<RouteDto> getAllRoutesByUserId(Long userId) {
        return routeMapper.toDto(routeRepository.findByUser_id(userId)
                .stream()
                .toList());
    }

    public List<RouteDto> getAllRoutesByUserId(Long userId, Integer pageNo, Integer pageSize) {
        return routeMapper.toDto(routeRepository.findByUser_id(userId, PageRequest.of(pageNo, pageSize))
                .stream()
                .toList());
    }


    /**
     * Get all routes by filter
     * @param filter the search criteria
     * @return List of RouteDTO Objects, mapped from Entity
     */
    public List<RouteDto> getAllRoutesByFilter(RouteFilter filter) {

        Specification<Route> filterCriteria = RouteSpecifications.createRouteQuery(filter);

        return routeMapper.toDto(routeRepository.findAll(filterCriteria));
    }

    /**
     * Adds a route
     * @param routeDto RouteDTO Object with required fields
     */
    public void addRoute(RouteDto routeDto) {
        Route route = routeMapper.toEntity(routeDto);

        routeRepository.save(route);
    }

    /**
     * Updates a route
     * @param id the identifier of the route
     * @param routeDto RouteDTO Object updated
     */
    public void updateRoute(Long id, RouteDto routeDto) {
        routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException(id));

        routeRepository.save(routeMapper.toEntity(routeDto));
    }

    /**
     * Deletes a route
     * @param id the identifier of the route
     */
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}