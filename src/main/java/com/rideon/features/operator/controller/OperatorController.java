package com.rideon.features.operator.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rideon.entity.User;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.features.operator.service.OperatorService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/operators")
public class OperatorController {
	
	@Autowired
	OperatorService operatorService;
	
	@GetMapping("")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Page<User>> findAllOperators(
			@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        @RequestParam(defaultValue = "userName") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction){
		
		Sort sort = direction.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    Pageable pageable = PageRequest.of(page, size, sort);
	    
	    Page<User> operators = operatorService.findAllOperators(pageable);
		
		if(operators.isEmpty()) {
			throw new ResourceNotFoundException("No Operators found");
		}
		
		return new ResponseEntity<>(operators,HttpStatus.OK);
	}
	
	@GetMapping("/pending")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> findPendingOperator(){
        List<User> pendingOperators =  operatorService.findPendingOperator();

        if(pendingOperators.isEmpty()) {
            throw new ResourceNotFoundException("No Pending Operators found");
        }

        return new ResponseEntity<>(pendingOperators,HttpStatus.OK);
    }
	
	@PostMapping("")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<User> saveOperator(@RequestBody @Valid User operator) {
		User newOperator = operatorService.saveOperator(operator);
		
		return new ResponseEntity<>(newOperator,HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> deleteOperatorById(@PathVariable int id) {
	    
	    operatorService.findByOperatorId(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No Operator Available with this id : " + id));

	    operatorService.deleteOperatorById(id);
        //need to add operator in response
	    return ResponseEntity.ok("Operator deactivated successfully with id : " + id);
	}
	
	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> updateOperatorStatus(@PathVariable int id, @RequestParam String status){
		operatorService.findByOperatorId(id)
				.orElseThrow(() -> new ResourceNotFoundException("No Operator Available with this id : " + id));
		
		operatorService.updateOperatorStatus(id,status);
        //need to add operator in response
		return ResponseEntity.ok("Operator status updated successfully with id : " + id);
		
	}
}
