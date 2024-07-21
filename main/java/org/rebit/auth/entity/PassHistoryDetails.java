package org.rebit.auth.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "TBL_PASSWORD_HISTORY")
@Entity
@Data
public class PassHistoryDetails {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "N_ID")
		private Long id;
		
		@ManyToOne
	    @JoinColumn(name="N_USERID")
		private UserMaster userMaster;
		
		@Column(name = "S_PASSWORD")
		private String passCode;
		
		@Column(name = "S_CREATED_BY")
		private String creatorUserId;
		
		@CreationTimestamp
		@Column(name = "S_CREATED_ON")
		private Date createdAt;
		
}
