package com.rideon.features.operator.service;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rideon.entity.User;
import com.rideon.exception.ResourceNotFoundException;
import com.rideon.repository.OperatorRepository;

@Service
public class OperatorServiceImplementation implements OperatorService{
	
    @Autowired	
	OperatorRepository operatorRepository;
    
	public Page<User> findAllOperators(Pageable pageable){
		return operatorRepository.findAllOperators(pageable);
	}
	
	public List<User> findPendingOperator(){
		return operatorRepository.findAllPendingOperators();
	}
	
	public User saveOperator(User operator) {
		if(operator.getUserStatus() == null) {
			operator.setUserStatus("Active");
		}
		if(operator.getUserCreatedDate() == null) {
			operator.setUserCreatedDate(LocalDate.now());
		}
		return operatorRepository.save(operator);
	}
	
	public void deleteOperatorById(int id) {
		operatorRepository.deleteOperatorById(id);
	}
	
	public Optional<User> findByOperatorId(int id){
		return operatorRepository.findById(id);
	}
	
	public User updateOperatorStatus(int id, String status) {
	    int updated = operatorRepository.updateOperatorStatus(id, status);

	    if (updated == 0) {
	        throw new ResourceNotFoundException("No operator found with id: " + id);
	    }

	    return operatorRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No operator found with id " + id));
	}
}
