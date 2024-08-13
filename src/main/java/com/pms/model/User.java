package com.pms.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pms.config.CustomDateSerializer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User Model", description = "This Is The User Model")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Email(message = "Please Enter Valid Email ID")
	@NotNull(message = "Email is required")
	@NotBlank(message = "Email can not be blank")
	private String email;

	@NotNull(message = "Password is required")
	@NotBlank(message = "Password can not be blank")
//	@Length(min = 3, max = 10, message = "Password Must be 3 to 10 character.")
	private String password;

	@NotNull(message = "Name is required")
	@NotBlank(message = "Name can not be blank")
	@Length(min = 5, max = 20, message = "Full Name Must be 5 to 20 character.")
	private String name;

	@NotNull(message = "Mobile Number is required")
	@NotBlank(message = "Mobile Number can not be blank")
	@Length(min = 5, max = 15, message = "Mobile Number Must be 5 to 15 Digit.")
	private String mobNumber;

	@JsonSerialize(using = CustomDateSerializer.class)
	private Date regDate;

	@NotNull(message = "Please Select Any ROLE")
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "roleId")

	)
	private Set<Role> roles = new HashSet<>();

	public void setEmail(String email) {
		this.email = (email != null) ? email.toUpperCase() : null;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonManagedReference
	private Set<Products> products;
}
