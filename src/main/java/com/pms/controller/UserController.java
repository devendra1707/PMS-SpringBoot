package com.pms.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.exception.InvalidRoleIdException;
import com.pms.exception.SingleRegistrationException;
import com.pms.model.User;
import com.pms.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	@Operation(summary = "Registration Endpoint", description = "Allows registration for any user with the specified role.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Registration successfully completed"),
			@ApiResponse(responseCode = "400", description = "User is already registered"),
			@ApiResponse(responseCode = "404", description = "Invalid role ID") })

	public ResponseEntity<?> create(@Valid @RequestBody User user, @RequestParam("rId") Integer rId) {
		try {
			User registeredUser = userService.registerNewUser(user, rId);
			return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
		} catch (InvalidRoleIdException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Invalid role ID. Please choose 'ADMIN' OR 'USER' role only.");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		} catch (SingleRegistrationException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "User is already registered. Please log in.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/current-user")
	public ResponseEntity<?> getCurrentUser(Principal principal) {
		if (principal == null) {
			return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
		}

		User user = userService.getCurrentUser(principal);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

}
