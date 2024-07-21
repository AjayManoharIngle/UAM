package org.rebit.auth.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "TBL_OFFCLOCATION_M")
@Entity
public class OfficeLocationDetails {
	
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "N_OFFCLOCID")
		private long officeLocationId;
		
		@Column(name = "S_LOCATIONSN")
		private String locationShortName;
		
		@Column(name = "S_LOCATIONLN")
		private String locationLongName;
		
		@Column(name = "N_STATUS")
		private int status;
		
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

		public long getOfficeLocationId() {
			return officeLocationId;
		}

		public void setOfficeLocationId(long officeLocationId) {
			this.officeLocationId = officeLocationId;
		}

		public String getLocationShortName() {
			return locationShortName;
		}

		public void setLocationShortName(String locationShortName) {
			this.locationShortName = locationShortName;
		}

		public String getLocationLongName() {
			return locationLongName;
		}

		public void setLocationLongName(String locationLongName) {
			this.locationLongName = locationLongName;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getCreatorUserId() {
			return creatorUserId;
		}

		public void setCreatorUserId(String creatorUserId) {
			this.creatorUserId = creatorUserId;
		}

		public Date getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
		}

		public String getUpdaterUserId() {
			return updaterUserId;
		}

		public void setUpdaterUserId(String updaterUserId) {
			this.updaterUserId = updaterUserId;
		}

		public Date getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(Date updatedAt) {
			this.updatedAt = updatedAt;
		}
}
