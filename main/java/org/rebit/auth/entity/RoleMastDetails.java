package org.rebit.auth.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "TBL_ROLE_M")
@Entity
@Data
public class RoleMastDetails {


		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SeqRoleId")
		@SequenceGenerator(sequenceName = "SEQ_ROLE_ID",allocationSize = 1, name = "SeqRoleId")
		@Column(name = "N_ROLEID")
		private long roleId;
	
		@Column(name = "S_ROLENAME")
		private String roleName;
		
		@Column(name = "N_STATUS")
		private int status;
		
		@Column(name = "S_CREATEDBY")
		private String createdBy;
		
		@Column(name = "DT_CREATEDDATE")
		private Date createdOn;
		
		@Column(name = "S_UPDATEDBY")
		private String updateBy;
		
		@Column(name = "DT_UPDATEDDATE")
		private Date updatedOn;	
		
		@ManyToMany(mappedBy = "rolesDetails")
	    List<ApiMasterDetails>  apiMasterDetails;
		
}
