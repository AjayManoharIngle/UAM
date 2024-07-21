package org.rebit.auth.administrator.model;

import java.util.Set;

import lombok.Data;


@Data
public class LogFilesNameDto {
	
	Set<FileDetailDto> fileNames;
	
}
