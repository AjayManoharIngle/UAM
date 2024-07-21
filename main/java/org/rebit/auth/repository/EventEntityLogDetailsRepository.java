package org.rebit.auth.repository;



import org.rebit.auth.entity.EventEntityLogDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventEntityLogDetailsRepository extends JpaRepository<EventEntityLogDetails, Long>{
	
	
}
