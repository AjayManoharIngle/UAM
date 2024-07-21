package org.rebit.auth.repository;

import org.rebit.auth.entity.RoleMastDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RoleDetailsRepository extends JpaRepository<RoleMastDetails, Long>{
	RoleMastDetails findByRoleName(String roleName);
}
