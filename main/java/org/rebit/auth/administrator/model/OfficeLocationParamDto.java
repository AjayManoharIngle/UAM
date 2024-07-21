package org.rebit.auth.administrator.model;

import java.util.Set;

public class OfficeLocationParamDto {
	
	private Set<String> status;
	private Set<String> officeLocationName;
	private Set<String> officeLocationShortName;
	public Set<String> getStatus() {
		return status;
	}
	public void setStatus(Set<String> status) {
		this.status = status;
	}
	public Set<String> getOfficeLocationName() {
		return officeLocationName;
	}
	public void setOfficeLocationName(Set<String> officeLocationName) {
		this.officeLocationName = officeLocationName;
	}
	public Set<String> getOfficeLocationShortName() {
		return officeLocationShortName;
	}
	public void setOfficeLocationShortName(Set<String> officeLocationShortName) {
		this.officeLocationShortName = officeLocationShortName;
	}
	
	

}
