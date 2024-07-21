package org.rebit.auth.administrator.model;

import lombok.Data;

@Data
public class FileDetailDto {
	private String name;
	private String creationTime;
	private String lastAccessTime;
	private String lastModifiedTime;
}
