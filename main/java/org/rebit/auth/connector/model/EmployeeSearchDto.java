package org.rebit.auth.connector.model;

import lombok.Data;

@Data
public class EmployeeSearchDto {
	private String name;
	private String role;
	private String email;
	private String mobile;
	private String id;
	private String status;
	private Long phone;
	private String officeLocation;
	private String regionalOffice;
}
