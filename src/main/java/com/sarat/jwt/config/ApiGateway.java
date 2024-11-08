package com.sarat.jwt.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sarat.jwt.util.JwtUtil;

@Component
public class ApiGateway {
//    private JwtTokenGenerator jwtTokenGenerator;
    private JwtUtil jwtTokenValidator;

    public ApiGateway() {}

    public ApiGateway(JwtUtil jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    public void setJwtTokenGenerator(JwtUtil jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    public void setJwtTokenValidator(JwtUtil jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    public String generateToken(UserDetails userDetails) {
        return jwtTokenValidator.generateToken(userDetails);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtTokenValidator.validateToken(token,userDetails);
    }

    public boolean authenticate(String token,UserDetails userDetails) {
        if (validateToken(token, userDetails)) {
            // Token is valid, authenticate the user
            return true;
        } else {
            // Token is invalid, do not authenticate the user
            return false;
        }
    }
}
