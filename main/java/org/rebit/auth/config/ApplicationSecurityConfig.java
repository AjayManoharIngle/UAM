package org.rebit.auth.config;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rebit.auth.jwt.JwtConfig;
import org.rebit.auth.repository.TokenDetailsRepository;
import org.rebit.auth.service.impl.ApplicationUserService;
import org.rebit.auth.util.AuthKavachUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



/**
 * 
 * @author Kapil Gautam
 * 
 */

@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableMethodSecurity
public class ApplicationSecurityConfig {

	final static Logger logger = LogManager.getLogger();
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
	
   
	@Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService,
			SecretKey secretKey, JwtConfig jwtConfig, AuthKavachUtil authKavachUtil,
			TokenDetailsRepository tokenDetailsRepository) {
		super();
		logger.trace("Entry into ApplicationSecurityConfig");
		this.passwordEncoder = passwordEncoder;
		this.applicationUserService = applicationUserService;
		this.secretKey = secretKey;
		this.jwtConfig = jwtConfig;
		logger.trace("Exit from ApplicationSecurityConfig");
	}
	
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		logger.trace("Entry into configure");
    	//String[] apis = jwtConfig.getApplicationApiPermitall().split(",");
        http
        		.cors()
        		.and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //if you want to enable spring security functionality then enable below code
                //.addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig),UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .requestMatchers("/**").permitAll()
                .anyRequest()
                .authenticated();
        logger.trace("Exit from configure");
        return http.build();
    }

	@Autowired
	void registerProvider(AuthenticationManagerBuilder auth) {
    	logger.trace("Entry into configure");
    	auth.authenticationProvider(daoAuthenticationProvider());
        logger.trace("Exit from configure");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
    	logger.trace("Entry into daoAuthenticationProvider");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        logger.trace("Exit from daoAuthenticationProvider");
        return provider;
    }
    
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
