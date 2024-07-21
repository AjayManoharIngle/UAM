package org.rebit.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.rebit.auth.config.GlobalProperties;
import org.rebit.auth.service.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class RbiUserManagementConnector extends SpringBootServletInitializer implements WebApplicationInitializer{

//	@Autowired
//	private UserAccessManagementProperties userAccessManagementProperties;

	//@Value("${cors.allowedOrigin.hosts}")
//	private String corsAllowedHosts;
	
	@Autowired
	private GlobalProperties globalProperties;
	
	@Autowired
	private AuthServices authServices;

	public static void main(String[] args) {
		SpringApplication.run(RbiUserManagementConnector.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RbiUserManagementConnector.class);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// logger.debug("Adding Cors For Below Hosts:");
//				String hosts = userAccessManagementProperties.getCorsAllowedHosts();
				authServices.refreshApplicationProperties();
				if (globalProperties.getCorsAllowedHosts() != null) {
					// logger.debug(hosts);
					List<String> allowedHosts = getAllowedOriginHosts();
					if (allowedHosts.size() > 1) {
						// logger.debug("All CORS Allowed Hosts :"+allowedHosts);
						registry.addMapping("/**").allowedOrigins(getArrayFromList(allowedHosts)).allowedHeaders("*")
								.allowedMethods("*");

					} else {
						// logger.debug("Single CORS Allowed Hosts :"+allowedHosts);
						registry.addMapping("/**").allowedOrigins(globalProperties.getCorsAllowedHosts()).allowedHeaders("*")
								.allowedMethods("*");
					}

				} 
			}
		};
	}

	public String[] getArrayFromList(List<String> list) {

		return list.stream().toArray(String[]::new);
	}

	public List<String> getAllowedOriginHosts() {
		List<String> allowedHosts = new ArrayList<>();
		if (globalProperties.getCorsAllowedHosts() != null) {

			if (globalProperties.getCorsAllowedHosts().contains(",")) {
				allowedHosts.addAll(Arrays.asList(globalProperties.getCorsAllowedHosts().split(",")));
			} else {
				allowedHosts.add(globalProperties.getCorsAllowedHosts());
			}
			return allowedHosts;
		} else {
			return new ArrayList<>();
		}
	}
}
