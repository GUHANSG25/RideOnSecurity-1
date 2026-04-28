package com.rideon.features.policy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rideon.entity.Policy;
import com.rideon.repository.PolicyRepository;

@Service
public class PolicyServiceImplementation implements PolicyService{
	@Autowired
    private PolicyRepository policyRepository;

    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public void deletePolicy(int id) {
        policyRepository.deleteById(id);
    }
}
