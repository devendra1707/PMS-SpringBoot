package com.pms.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {

	@Email(message = "Please Enter Valid Email ID")
	@NotNull(message = "Email is required")
	@NotBlank(message = "Email can not be blank")
	private String email;

	@NotNull(message = "Password is required")
	@NotBlank(message = "Password can not be blank")
	private String password;

	public void setEmail(String email) {
		this.email = email = (email != null) ? email.toUpperCase() : null;
	}

}
