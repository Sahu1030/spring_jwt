package com.sarat.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.sarat.jwt.util.JwtUtil;
@Component
public class ApiGatewayConfig {

	public ApiGatewayConfig() {
		// TODO Auto-generated constructor stub
	}
	
	    @Bean
	    public ApiGateway apiGateway() {
	        // Create an API Gateway instance
	        ApiGateway apiGateway = new ApiGateway();
	        // Set the JWT token generator and validator
	        apiGateway.setJwtTokenGenerator(jwtTokenGenerator());
	        apiGateway.setJwtTokenValidator(jwtTokenValidator());
	        return apiGateway;
	    }
	    
	    @Bean
	    public JwtUtil jwtTokenGenerator() {
	        return new JwtUtil();
	    }
	    
	    @Bean
	    public JwtUtil jwtTokenValidator() {
	        return new JwtUtil();
	    }
	}

