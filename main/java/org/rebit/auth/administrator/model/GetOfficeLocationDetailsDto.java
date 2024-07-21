package org.rebit.auth.administrator.model;

import lombok.Data;

@Data
public class GetOfficeLocationDetailsDto {
	private Long officeLocationId;
	private String officeLocationName;
	private String officeLocationShortName;
	private String status;
}
