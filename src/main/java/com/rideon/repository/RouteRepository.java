package com.rideon.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rideon.entity.Route;

import jakarta.transaction.Transactional;

public interface RouteRepository extends JpaRepository<Route, Integer> {
	@Modifying
	@Transactional
	@Query("update Route r set r.status = 'InActive' where r.routeId = :id")
	public int deleteRoute(@Param("id") int id);
	
	public Route findBySource(@Param("source") String source);
}
