package org.rebit.auth.administrator.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddOfficeLocationDetailsDto {

	@Ascii
	@NotBlank(message = "{AddOfficeLocationDetailsDto.offLocName.notempty}")
	private String officeLocationName;
	@Ascii
	@NotBlank(message = "{AddOfficeLocationDetailsDto.offLocShortName.notempty}")
	private String officeLocationShortName;
	@Ascii
	@NotBlank(message = "{updateEmployeeDetailsDto.status.notempty}")
	@Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "{status.only.allowed}")
	private String status;
	public String getOfficeLocationName() {
		return officeLocationName;
	}
	public void setOfficeLocationName(String officeLocationName) {
		this.officeLocationName = officeLocationName;
	}
	public String getOfficeLocationShortName() {
		return officeLocationShortName;
	}
	public void setOfficeLocationShortName(String officeLocationShortName) {
		this.officeLocationShortName = officeLocationShortName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
