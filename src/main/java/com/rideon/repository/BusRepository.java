package com.rideon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rideon.entity.Bus;

import jakarta.transaction.Transactional;

public interface BusRepository extends JpaRepository<Bus, Integer> {
	
	@Query("SELECT b FROM Bus b WHERE b.operator.userId = :operatorId")
    List<Bus> findByOperatorId(@Param("operatorId") int operatorId);
	
	@Query("SELECT b FROM Bus b WHERE b.busStatus = 'ACTIVE'")
    public List<Bus> findAllActiveBuses();
	
	@Modifying 
	@Transactional
	@Query("update Bus b set b.busStatus = 'InActive' where b.busId = :id")
	public int deactivateBus(@Param("id") int id);
	
	@Modifying 
	@Transactional
	@Query("update Bus b set b.busStatus = :status where b.busId = :id")
	public int updateBusStatus(@Param("id") int id,@Param("status") String status);
	
	@Query("SELECT b FROM Bus b WHERE b.busNumber =:busNo")
	public Bus findByBusNumber(String busNo);
	
//	@Query("select b.busPolicy from Bus b where b.busId = :id")
//	public String findPolicyByBusId(@Param("id") int id);
}
