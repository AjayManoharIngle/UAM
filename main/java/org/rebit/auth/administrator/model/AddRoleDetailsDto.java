package org.rebit.auth.administrator.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddRoleDetailsDto {
	
	@Ascii
	@NotBlank(message = "{AddRoleDetailsDto.rolName.notempty}")
	private String roleName;
	
	@Ascii
	@NotBlank(message = "{updateEmployeeDetailsDto.status.notempty}")
	@Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "{status.only.allowed}")
	private String status;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
