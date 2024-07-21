package org.rebit.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@PropertySource("classpath:master.properties")
@Getter
public class MasterProperties {
	
	@Value("${document.size.in.mb}")
    private Integer documentSizeInMb;
	@Value("${document.type.pdf}")
    private String documentTypePdf;
	
}