package com.unina.springnatour.repository;

import com.unina.springnatour.model.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
