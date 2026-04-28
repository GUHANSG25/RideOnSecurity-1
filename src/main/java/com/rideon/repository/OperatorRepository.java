package com.rideon.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rideon.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface OperatorRepository extends JpaRepository<User, Integer> {
	@Modifying
	@Transactional
	@Query("update User o  set o.userStatus = 'InActive' where o.userId = :id")
	public int deleteOperatorById(@Param("id") int id);
	
	@Query("select o from User o where o.userRole = 'ROLE_OPERATOR' and userStatus <> 'PENDING'")
	Page<User> findAllOperators(Pageable pageable);
	
	@Query("select o from User o where o.userRole = 'ROLE_OPERATOR' and userStatus = 'PENDING'")
	List<User> findAllPendingOperators();
	
	@Modifying
	@Transactional
	@Query("update User o set o.userStatus = :status where o.userId = :id")
	int updateOperatorStatus(@Param("id") int id, @Param("status") String status);
}
