package com.rideon.features.policy.service;

import java.util.List;

import com.rideon.entity.Policy;

public interface PolicyService {
	Policy createPolicy(Policy policy);
	List<Policy> getAllPolicies();
	void deletePolicy(int id);
	
}
