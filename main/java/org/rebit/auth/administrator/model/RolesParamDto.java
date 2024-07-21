package org.rebit.auth.administrator.model;

import java.util.Set;

public class RolesParamDto {
	
	private Set<String> status;
	private Set<String> roleName;
	public Set<String> getStatus() {
		return status;
	}
	public void setStatus(Set<String> status) {
		this.status = status;
	}
	public Set<String> getRoleName() {
		return roleName;
	}
	public void setRoleName(Set<String> roleName) {
		this.roleName = roleName;
	}
	
	

}
