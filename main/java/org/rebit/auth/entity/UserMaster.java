package org.rebit.auth.entity;

import java.util.Date;
import java.util.List;

import org.rebit.auth.disha.entity.ProjectRegistration;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_USER_M")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserMaster {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "N_USERID")
		private long userId;

		@Column(name = "S_USEREMAILID")
		private String userEmailId;

		@Column(name = "S_USERNAME")
		private String userName;

		@Column(name = "S_FIRSTNAME")
		private String firstName;
		
		@Column(name = "S_LASTNAME")
		private String lastName;
		
		@Column(name = "DT_LASTLOGINDATE")
		private Date lastLoginDate;

		@OneToOne
		@JoinColumn(name="S_ROCODE")
		private RegionalOfficeDetails regionalOfficeDetails;		
		
		@OneToOne
		@JoinColumn(name="N_OFFCLOCID")
		private OfficeLocationDetails officeLocationDetails;
		
		
		@OneToMany(mappedBy="userMaster")
		@OrderBy("id DESC")
		private List<PassHistoryDetails> passHistoryDetails;
		
				
		@ManyToMany(cascade = { CascadeType.ALL })
	    @JoinTable(
	        name = "TBL_USER_ROLE_MAPPING", 
	        joinColumns = { @JoinColumn(name = "N_USERID") }, 
	        inverseJoinColumns = { @JoinColumn(name = "N_ROLEID") }
	    )
	    private List<RoleMastDetails> rolesDetails;

		@Column(name = "N_USERMOBNO")
		private String userMobileNo;
		
		@Column(name = "N_STATUS")
		private Long status;
		
		@Column(name = "S_CREATEDBY")
		private String creatorUserId;
		
		@Column(name = "DT_CREATEDDATE")
		private Date createdAt;
		
		@Column(name = "S_UPDATEDBY")
		private String updaterUserId;
		
		@Column(name = "DT_UPDATEDDATE")
		private Date updatedAt;		
		
		@Column(name = "S_PASSWORD")
		private String passCode;
		
		@Column(name = "N_ISLDAPUSER")
		private Long isLDAPUser;
		
		@Column(name = "N_UNSUCCESS_ATTEMPT")
		private Long unSuccessAttempt;
		
		@Column(name = "DT_BLOCK_TILL")
		private Date blockedTill;
		
		@Column(name = "DT_PASS_EXPIRED")
		private Date passExpired;
		
		@Column(name = "DT_LAST_FAILED_ATTEMPT")
		private Date lastFailedAttempt;
		
		@OneToOne
		@JoinColumn(name="N_PROJECT_ID")
		private  ProjectRegistration projectRegistration;
		
}