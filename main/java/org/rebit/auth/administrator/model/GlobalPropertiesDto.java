package org.rebit.auth.administrator.model;

import org.rebit.auth.validation.constraint.Ascii;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Component
public class GlobalPropertiesDto {
	@Ascii
	@NotBlank(message = "ldapUrl can not be null or empty")
	private String ldapUrl;
	@Ascii
	private String dnUsernameAttribute;
	@Ascii
	private String domainName;
	@Ascii
	@NotBlank(message = "baseDn can not be null or empty")
	private String baseDn;
	@Ascii
	@NotBlank(message = "searchFilterObject can not be null or empty")
	private String searchFilterObject;
	@Ascii
	@NotBlank(message = "dnForSearchFilter can not be null or empty")
	private String dnForSearchFilter;
	@Ascii
	@NotBlank(message = "usernameAttribute can not be null or empty")
	private String usernameAttribute;
	@Ascii
	private String mobileAttribute;
	@Ascii
	@NotBlank(message = "firstnameAttribute can not be null or empty")
	private String firstnameAttribute;
	@Ascii
	@NotBlank(message = "lastnameAttribute can not be null or empty")
	private String lastnameAttribute;
	@Ascii
	@NotBlank(message = "emailAttribute can not be null or empty")
	private String emailAttribute;
	@Ascii
	@Pattern(regexp = "^(true|false)$", message = "Generate Mobile Number Only true and false")
	@NotBlank(message = "generateMobileNumber can not be null or empty")
	private String generateMobileNumber;
	
}