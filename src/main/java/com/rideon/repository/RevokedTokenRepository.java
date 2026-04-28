package com.rideon.repository;

import org.springframework.data.repository.CrudRepository;

import com.rideon.entity.RevokedToken;

public interface RevokedTokenRepository extends CrudRepository<RevokedToken, String> {
	boolean existsByToken(String token);
}
