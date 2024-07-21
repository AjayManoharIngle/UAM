package org.rebit.auth.config;

import java.util.ArrayList;
import java.util.List;

import org.rebit.auth.entity.ApiMasterDetails;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class GlobalProperties {
	private String ldapUrl;
	private String dnUsernameAttribute;
	private String domainName;
	private String baseDn;
	private String searchFilterObject;
	private String dnForSearchFilter;
	private String usernameAttribute;
	private String mobileAttribute;
	private String firstnameAttribute;
	private String lastnameAttribute;
	private String emailAttribute;
	private String generateMobileNumber;
	private String corsAllowedHosts;
	private String otpSmsIntegrated;
	
	private List<ApiMasterDetails> listOfGetApis;
	private List<ApiMasterDetails> listOfPostApis;
	private List<ApiMasterDetails> listOfPatchApis;
	private List<ApiMasterDetails> listOfPutApis;
	private List<ApiMasterDetails> listOfDeleteApis;
	private List<ApiMasterDetails> listOfOptionApis;
	
	
	public List<ApiMasterDetails> findByMethodType(String methodType){
		methodType = methodType.toUpperCase();
		switch (methodType) {
		case "GET":
			return listOfGetApis;
		case "POST":
			return listOfPostApis;
		case "PATCH":
			return listOfPatchApis;
		case "PUT":
			return listOfPutApis;
		case "DELETE":
			return listOfDeleteApis;	
		case "OPTION":
			return listOfOptionApis;
		case "ALL":
			List<ApiMasterDetails> all =new ArrayList<ApiMasterDetails>();
			all.addAll(listOfGetApis);
			all.addAll(listOfPostApis);
			all.addAll(listOfPatchApis);
			all.addAll(listOfPutApis);
			all.addAll(listOfDeleteApis);
			all.addAll(listOfOptionApis);
			return all;
		default:
			return new ArrayList<>();
		}
	}
}