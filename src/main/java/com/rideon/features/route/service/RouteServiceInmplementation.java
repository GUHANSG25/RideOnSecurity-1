package com.rideon.features.route.service;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rideon.entity.Route;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.repository.RouteRepository;

@Service
public class RouteServiceInmplementation implements RouteService{
	@Autowired
	RouteRepository routeRepository;
	
	public Page<Route> findAllRoute(Pageable pageable){
		return routeRepository.findAll(pageable);
	}
	
	public Route createRoute(Route route) {
		return routeRepository.save(route);
	}
	
	public void deleteRouteById(int id) {
		routeRepository.deleteRoute(id);
	}
	
	public Optional<Route> findRouteById(int id) {
    	return routeRepository.findById(id);
    }
	
	public Route updateRoute(int id,Route request) {
		Route route = routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route is not found"));
		
		if(request.getSource() != null) {
			route.setSource(request.getSource());
		}
		if(request.getDestination() != null) {
			route.setDestination(request.getDestination());
		}
		if(request.getDistance() != null && !request.getDistance().equals(route.getDistance())) {
		    route.setDistance(request.getDistance());
		}
		if(request.getEstimatedTime() != null) {
			route.setEstimatedTime(request.getEstimatedTime());
		}
		if(request.getStatus() != null) {
			route.setStatus(request.getStatus());
		}
		
		return routeRepository.save(route);
	}
	
	public Route searchRouteBySource(String source) {
		Route route = routeRepository.findBySource(source);
		
		if(route == null) {
			throw new ResourceNotFoundException("No Route available with the searched source");
		}
		
		return route;
	}
	
	public Optional<Route> findByRouteId(int id) {
		return routeRepository.findById(id);
	}
}
