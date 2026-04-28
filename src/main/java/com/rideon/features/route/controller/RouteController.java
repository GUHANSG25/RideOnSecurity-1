package com.rideon.features.route.controller;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rideon.entity.Route;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.features.route.dto.RouteResponse;
import com.rideon.features.route.service.RouteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/routes")
public class RouteController {
	@Autowired
    RouteService routeService;
	
	@GetMapping("")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_OPERATOR')")
	public ResponseEntity<Page<RouteResponse>> findAllRoute(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        @RequestParam(defaultValue = "source") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {

	    Sort sort = direction.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<Route> routes = routeService.findAllRoute(pageable);

	    if (routes.isEmpty()) {
	        throw new ResourceNotFoundException("No Routes Available");
	    }

	    // ✅ Convert using map()
	    Page<RouteResponse> routesRes = routes.map(r -> {
	        RouteResponse res = new RouteResponse();
	        res.setRouteId(r.getRouteId());
	        res.setSource(r.getSource());
	        res.setDestination(r.getDestination());
	        res.setDistance(r.getDistance());
	        res.setEstimatedTime(r.getEstimatedTime());
	        res.setStatus(r.getStatus());
	        return res;
	    });

	    return new ResponseEntity<>(routesRes, HttpStatus.OK);
	}
	
	@PostMapping("")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Route> createRoute(@Valid @RequestBody Route route){
		
		Route newRoute = routeService.createRoute(route);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newRoute);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_OPERATOR')")
	public ResponseEntity<Route> findByRouteId(@PathVariable Integer id) {
	    Route route = routeService.findRouteById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("No route found with id: " + id));
	    return new ResponseEntity<>(route, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Route> deleteRouteById(@PathVariable int id){
		
		Route route = routeService.findByRouteId(id)
				.orElseThrow(() -> new ResourceNotFoundException("No Route Available with this id : " + id));

		routeService.deleteRouteById(id);
		return ResponseEntity.ok(route);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Route> updateRoute(@PathVariable int id,@RequestBody Route request){
		return ResponseEntity.ok(routeService.updateRoute(id,request));
	}
	
	@GetMapping("/search")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Route> searchRouteBySource(@RequestParam String source){
		return ResponseEntity.status(HttpStatus.OK).body(routeService.searchRouteBySource(source));
	}
}
