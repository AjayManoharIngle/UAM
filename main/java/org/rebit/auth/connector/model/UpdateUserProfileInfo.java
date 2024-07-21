package org.rebit.auth.connector.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UpdateUserProfileInfo {

	@Ascii
	@NotBlank(message = "{primaryEmail.notempty}")
	@Pattern(regexp = "^([a-zA-Z0-9_.-])+\\@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$", message = "{primaryEmail.emailCheck}")
	private String userEmailId;
	@Ascii
	@NotBlank(message = "{firstName.notempty}")
	@Pattern(regexp = "^[A-Za-z0-9 .]+$", message = "{firstName.alphaNumeric}")
	private String firstName;
	@Ascii
	@NotBlank(message = "{lastName.notempty}")
	@Pattern(regexp = "^[A-Za-z0-9 .]+$", message = "{lastName.alphaNumeric}")
	private String lastName;
	@Ascii
	@NotBlank(message = "{regionalOffice.notempty}")
	private String regionalOfficeDetails;	
	@Ascii
	@NotBlank(message = "{officeLocation.notempty}")
	private String officeLocationDetails;
	@Ascii
	@Size(max=10, message="{mobileNo.max.size}")
	@Pattern(regexp = "^((?!(0))[0-9]{10})$", message = "{mobileNo.invalid}")
    private String userMobileNo;
	private Long isLDAPUser;
    private String passCode;
    
}
