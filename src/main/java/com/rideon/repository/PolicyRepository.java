package com.rideon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rideon.entity.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {

}
