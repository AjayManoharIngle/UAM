package org.rebit.auth.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_REGIONAL_OFFICE_M")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegionalOfficeDetails {
	
	@Id
	@Column(name = "S_ROCODE")
	private String roCode;

	@Column(name = "S_RONAME")
	private String roName;

	@Column(name = "S_CREATEDBY")
	private String createdBy;

	@Column(name = "DT_CREATEDDATE")
	private Date createdOn;

	@Column(name = "S_UPDATEDBY")
	private String updatedBy;

	@Column(name = "DT_UPDATEDDATE")
	private Date updatedOn;		
	
}
