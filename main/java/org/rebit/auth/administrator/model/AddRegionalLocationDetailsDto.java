package org.rebit.auth.administrator.model;

import org.rebit.auth.validation.constraint.Ascii;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class AddRegionalLocationDetailsDto {

	@Ascii
	@NotBlank(message = "{AddRegionalLocationDetailsDto.code.notempty}")
	private String code;
	@Ascii
	@NotBlank(message = "{AddRegionalLocationDetailsDto.name.notempty}")
	private String name;
	@Ascii
	@NotBlank(message = "{AddRegionalLocationDetailsDto.status.notempty}")
	@Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "{status.only.allowed}")
	private String status;	
	
}
