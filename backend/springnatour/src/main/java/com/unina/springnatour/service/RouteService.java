package com.unina.springnatour.service;

import com.unina.springnatour.dto.route.RouteDto;
import com.unina.springnatour.dto.route.RouteMapper;
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


    public List<RouteDto> getAllRoutes() {
//        return routeMapper.toDto(routeRepository.findAll()
//                .stream()
//                .toList());
        List<Route> routes = routeRepository.findAll().stream().toList();
        Double longitude = routes.get(0).getStops().get(0).getLocation().getLongitude();
        System.out.println(longitude);
        return routeMapper.toDto(routes);
    }
}
