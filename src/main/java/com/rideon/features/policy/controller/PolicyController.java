package com.rideon.features.policy.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rideon.entity.Policy;
import com.rideon.features.bus.service.BusService;
import com.rideon.features.policy.service.PolicyService;

@RequestMapping("/policy")
public class PolicyController {
	
	@Autowired
	PolicyService policyService;
	
	@Autowired 
	BusService busService;
	
	@PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) {
        return new ResponseEntity<>(policyService.createPolicy(policy),HttpStatus.CREATED);
    }
    
    
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<List<Policy>> getAllPolicies() {
        return new ResponseEntity<>(policyService.getAllPolicies(),HttpStatus.OK);
    }
    
    
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public void deletePolicy(@PathVariable int id) {
//        policyService.deletePolicy(id);
//    }
    
    @PostMapping("/{busId}/policies/{policyId}")
    @PreAuthorize("hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<String> assignPolicyToBus(@PathVariable int busId, @PathVariable int policyId) {

        busService.assignPolicyToBus(busId, policyId);
        return ResponseEntity.ok("Policy assigned successfully");
    }
    
    @GetMapping("/{busId}/policies")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_OPERATOR')")
    public ResponseEntity<List<Policy>> getPoliciesByBus(@PathVariable int busId) {
        return new ResponseEntity<>(busService.getPoliciesByBusId(busId),HttpStatus.OK);
    }
}
