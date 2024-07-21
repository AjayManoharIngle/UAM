package org.rebit.auth.administrator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateStatusDto {
	
	@NotBlank(message = "{updateEmployeeDetailsDto.status.notempty}")
	@Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "{status.only.allowed}")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
