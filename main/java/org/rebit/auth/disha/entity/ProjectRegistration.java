package org.rebit.auth.disha.entity;

import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_PROJECT_REGISTRATION")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "N_PROJECT_ID")
	private Long projectId;
	
	@Column(name = "S_PROJECT_TITLE")
	private String projectTitle;
	
	@Column(name = "S_PROJECT_DESC")
	private String proejctDesc;
	
	@Column(name = "S_PROJECT_MANAGER_FIRST_NAME")
	private String projectManagerFirstName;
	
	@Column(name = "S_PROJECT_MANAGER_LAST_NAME")
	private String projectManagerLastName;
	
	@Column(name = "S_PROJECT_MANAGER_CONTACT_NO")
	private String projectManagerContactNo;
	
	@Column(name = "S_PROJECT_MANAGER_EMAIL")
	private String projectManagerEmail;
	
	@Column(name = "S_PROJECT_MANAGER_ROLE")
	private String projectManagerRole;
	
	@Column(name = "S_DEPARTMENT")
	private String department;
	
	@Column(name = "S_DEPARTMENT_BO_NAME")
	private String departmentBOName;
	
	@OneToMany(mappedBy="projectRegistration",cascade = { CascadeType.ALL })
	@OrderBy("id DESC")
	private ArrayList<TeamDetails> teamDetails;
	
	@Column(name = "S_UPLOAD_PROJECT_SWO")
	@OneToOne(mappedBy="projectRegistration",cascade = { CascadeType.ALL })
	private FileUpload uploadProjectSWO;
	
	@Column(name = "S_UPLOAD_PROJECT_SRS")
	@OneToOne(mappedBy="projectRegistration",cascade = { CascadeType.ALL })
	private FileUpload uploadProjectSRS;
	
	@Column(name = "S_UPLOAD_PROJECT_PREREQUISITE")
	@OneToOne(mappedBy="projectRegistration",cascade = { CascadeType.ALL })
	private FileUpload uploadProjectPrerequisite;
}
