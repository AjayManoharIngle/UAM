package org.rebit.auth.administrator.model;

import lombok.Data;

@Data
public class GetRoleDetailsDto {
	private Long roleId;
	private String roleName;
	private String status;
}
