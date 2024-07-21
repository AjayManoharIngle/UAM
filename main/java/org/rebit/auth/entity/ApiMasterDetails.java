package org.rebit.auth.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * 
 * @author Kapil Gautam
 * 
 */

@Entity
@Table(name = "tbl_api_m")
public class ApiMasterDetails {    
  
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SeqApiId")
	@SequenceGenerator(sequenceName = "SEQ_API_ID",allocationSize = 1, name = "SeqApiId")	
	@Column(name = "N_API_ID")
	private long apiId;
	
	@Column(name = "S_URI")
	private String uri;
	@Column(name = "S_METHOD")
	private String method;
	@Column(name = "N_IS_ACTIVE")
	private int status;
	@Column(name = "DT_CREATED_ON")
	private Date createdDate;
	@Column(name = "S_CREATED_BY")
	private String createdBy;
	@Column(name = "DT_UPDATED_ON")
	private Date updateDate;
	@Column(name = "S_UPDATED_BY")
	private String updateBy;
	@Column(name = "N_IS_PUBLIC")
	private Boolean permitAll;


	@ManyToMany(cascade = { CascadeType.ALL },fetch = FetchType.EAGER)
    @JoinTable(
        name = "tbl_role_api_access_mapping", 
        joinColumns = { @JoinColumn(name = "S_API_ID") }, 
        inverseJoinColumns = { @JoinColumn(name = "N_ROLEID") }
    )
    private List<RoleMastDetails> rolesDetails;

	
	public Boolean getPermitAll() {
		return permitAll;
	}

	public void setPermitAll(Boolean permitAll) {
		this.permitAll = permitAll;
	}
	
	public long getApiId() {
		return apiId;
	}

	public void setApiId(long apiId) {
		this.apiId = apiId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public List<RoleMastDetails> getRolesDetails() {
		return rolesDetails;
	}

	public void setRolesDetails(List<RoleMastDetails> rolesDetails) {
		this.rolesDetails = rolesDetails;
	}
       
}