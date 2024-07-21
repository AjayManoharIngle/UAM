package org.rebit.auth.administrator.model;

import java.util.Set;

import lombok.Data;

@Data
public class RegionalOfficeParamDto {
	private Set<String> status;
	private Set<String> code;
	private Set<String> name;
}
