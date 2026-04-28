package com.rideon.features.route.service;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rideon.entity.Route;


public interface RouteService {
	public Page<Route> findAllRoute(Pageable pageable);
	public Route createRoute(Route route);
	public void deleteRouteById(int id);
	public Optional<Route> findByRouteId(int id);
	public Route updateRoute(int id,Route request);
	public Route searchRouteBySource(String source);
	public Optional<Route> findRouteById(int id);
}
