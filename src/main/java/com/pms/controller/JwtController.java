package com.pms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.model.JwtRequest;
import com.pms.model.JwtResponse;
import com.pms.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication Model", description = "Here Login Every One What Are the Person We Do The Registration")
public class JwtController {

	@Autowired
	private JwtService jwtService;

	@PostMapping("/login")
	@Operation(summary = "Login Endpoint", description = "Allows users to log in with their credentials.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Login successfully"),
			@ApiResponse(responseCode = "401", description = "Invalid username or password. Please try again."),
			@ApiResponse(responseCode = "402", description = "User not found") })
	public ResponseEntity<?> createJwtToken(@Valid @RequestBody JwtRequest jwtRequest) {
		try {
			JwtResponse jwtResponse = jwtService.createJwtToken(jwtRequest);
			return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
		} catch (UsernameNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Invalid username or password. Please try again.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} catch (BadCredentialsException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Invalid username or password. Please try again.");
			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		}
	}

}
