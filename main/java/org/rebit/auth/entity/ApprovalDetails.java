package org.rebit.auth.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "TBL_APPROVAL_FLOW")
@Entity
@Data
public class ApprovalDetails {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "N_APPROVAL_ID")
		private long approvalId;
		
		@Column(name = "N_FLOW_TYPE_ID")
		private Long flowTypeId;
		
		@Column(name = "N_FLOW_TYPE_SUB_ID")
		private Long flowTypeSubId;
		
		@Column(name = "S_FLOW_TYPE")
		private String flowType;
		
		@Column(name = "S_ACTION")
		private String action;
		
		@Column(name = "S_MAKER")
		private String maker;
		
		@Column(name = "S_CHECKER")
		private String checker;
		
		@Column(name = "S_MAKER_COMMENTS")
		private String makerComments;
		
		@Column(name = "S_CHECKER_COMMENTS")
		private String checkComments;
		
		@Column(name = "S_REALM")
		private String realm;
		
		@Column(name = "S_CREATEDBY")
		private String creatorUserId;
		
		@CreationTimestamp
		@Column(name = "DT_CREATEDDATE")
		private Date createdAt;
		
		@Column(name = "S_UPDATEDBY")
		private String updaterUserId;
		
		@UpdateTimestamp
		@Column(name = "DT_UPDATEDDATE")
		private Date updatedAt;
		
		@Column(name = "S_DOCUMENT_NAME")
		private String documentName;

		@Lob
		@Column(name = "B_DOCUMENT", columnDefinition = "VARBINARY")
		private byte[] document;

}
