package com.skybound.springboot.sfdcplatformevent.repository;

import com.skybound.springboot.sfdcplatformevent.entity.LeadEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeadRepo extends JpaRepository<LeadEntity, Long> {

	@Query("SELECT p FROM LeadEntity p WHERE p.sfid = :sfId Or (p.firstName = :firstName and p.lastName = :lastName)")
	LeadEntity findBySfId(@Param("sfId") String sfId, @Param("firstName") String firstName,
			@Param("lastName") String lastName);

	LeadEntity findBySfid(String sdid);
}
