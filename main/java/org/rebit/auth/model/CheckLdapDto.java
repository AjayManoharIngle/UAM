package org.rebit.auth.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CheckLdapDto {

	@NotBlank(message = "{userName.notempty}")
	@Ascii
	private String userName;

	@NotBlank(message = "{password.notempty}")
	@Ascii
	private String password;
	
}
