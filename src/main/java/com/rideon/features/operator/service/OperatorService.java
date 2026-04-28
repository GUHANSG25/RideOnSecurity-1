package com.rideon.features.operator.service;

import java.util.List;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rideon.entity.User;

public interface OperatorService {
	public Page<User> findAllOperators(Pageable pageable);
	public List<User> findPendingOperator();
	public User saveOperator(User operator);
	public void deleteOperatorById(int id);
	public Optional<User> findByOperatorId(int id);
	public User updateOperatorStatus(int id,String status);
}
