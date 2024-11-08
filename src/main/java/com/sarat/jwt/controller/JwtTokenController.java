package com.sarat.jwt.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sarat.jwt.model.LoginRequest;
import com.sarat.jwt.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestController
@RequestMapping("/api/auth")
public class JwtTokenController {

	private static final Logger log = Logger.getLogger(JwtTokenController.class.getName());

	@Autowired
	private JwtUtil jwtUtil;

//    @Autowired
//    private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	private AuthenticationManager authenticationManager;

	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
		try {
			System.out.println("Inside login  check!!");
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			System.out.println("==================>1" + authentication.toString());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			System.out.println("Inside login  token generate!!");

			String token = jwtUtil.generateToken(userDetails);
			System.out.println("Final token check!!");

			return ResponseEntity.ok(token);
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	@GetMapping("/validate-token")
	public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
		try {
			String jwtToken = token.substring(7).trim();
			System.out.println(jwtToken);
			String username = jwtUtil.getUsernameFromToken(token);
			System.out.println(username);
//		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			boolean isValid = jwtUtil.validateToken(token, userDetails);
			System.out.println("validate-token check!!");
			return ResponseEntity.ok(isValid);
		} catch (SignatureException e) {
			// Return false when the signature does not match
			return ResponseEntity.ok(false);
		}
	}

	@GetMapping("/refresh-token")
	public ResponseEntity<String> refreshToken(HttpServletRequest request) {
		try {
			Claims claims = jwtUtil.resolveClaims(request);
			System.out.println("refresh-token check!!" + claims);

			if (claims != null && jwtUtil.validateClaims(claims)) {
//			String token = jwtUtil.generateToken((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
				String username = claims.getSubject(); // Extract the username from the Claims
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				String token = jwtUtil.generateToken(userDetails);
				return ResponseEntity.ok(token);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (Exception e) {
			if (e instanceof SignatureException) {
				System.out.println("Invalid JWT signature.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			} else if (e instanceof MalformedJwtException) {
				System.out.println("Invalid JWT token.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			} else {
				System.out.println("An error occurred: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}
	}

	@GetMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		SecurityContextHolder.clearContext();
		System.out.println("calling logout!!");
		return ResponseEntity.ok().build();
	}

	@GetMapping("/check")
	public ResponseEntity<String> check(HttpServletRequest request) {
		SecurityContextHolder.clearContext();
		System.out.println("calling check!!");
		String str = "checking...!!";
		return ResponseEntity.ok(str);
	}

}