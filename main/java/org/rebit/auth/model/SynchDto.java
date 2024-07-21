package org.rebit.auth.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SynchDto {

	@Ascii
	@NotBlank(message = "{SynchDto.userName.not.empty}")
	private String userName;
	@Ascii
	@NotBlank(message = "{SynchDto.password.not.empty}")
	private String password;
	
}
