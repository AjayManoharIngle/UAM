package org.rebit.auth.config;

import javax.crypto.SecretKey;

import org.rebit.auth.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

/**
 * 
 * @author Kapil Gautam
 * 
 */

@Configuration
public class JwtSecretKey {
	
	@Value("${application.jwt.secretKey}")
	private String secretKey;

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtSecretKey(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
