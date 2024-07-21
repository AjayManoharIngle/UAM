package org.rebit.auth.repository;

import java.util.List;

import org.rebit.auth.entity.RoleMastDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleMasterRepository extends JpaRepository<RoleMastDetails, Long>{ 

	public RoleMastDetails findByRoleIdAndStatus(long roleId,Integer status);
	public List<RoleMastDetails> findByRoleIdInAndStatus(List<Long> ids,Integer status);
	public RoleMastDetails findByRoleNameAndStatus(String roleName,Integer status);
	public List<RoleMastDetails> findByStatus(Integer status);
	public List<RoleMastDetails> findByRoleNameInAndStatus(List<String> roleNames,Integer status);
	
}
