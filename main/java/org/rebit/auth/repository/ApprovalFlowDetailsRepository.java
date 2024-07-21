package org.rebit.auth.repository;

import org.rebit.auth.entity.ApprovalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalFlowDetailsRepository extends JpaRepository<ApprovalDetails, Long>{
	
}
