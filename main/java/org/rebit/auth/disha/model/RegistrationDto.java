package org.rebit.auth.disha.model;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RegistrationDto {

	private String projectTitle;
	private String proejctDesc;
	private String projectManagerFirstName;
	private String projectManagerLastName;
	private String projectManagerContactNo;
	private String projectManagerEmail;
	private String projectManagerrole;
	private String department;
	private String departmentBOName;
	private ArrayList<SupportUser> teamDetails;
	private MultipartFile uploadProjectPrerequisite;
	private MultipartFile uploadProjectSRS;
	private MultipartFile uploadProjectSWO;
}
