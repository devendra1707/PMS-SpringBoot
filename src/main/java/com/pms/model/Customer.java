package com.pms.model;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pms.config.CustomDateSerializer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cusId;

	@NotNull(message = "Name is required")
	@NotBlank(message = "Name can not be blank")
	@Length(min = 5, max = 20, message = "Full Name Must be 5 to 20 character.")
	private String cusName;
	private String cusEmail;
	private String mobNum;
	private String address;

	@JsonSerialize(using = CustomDateSerializer.class)
	private Date regDate;

	public void setEmail(String cusEmail) {
		this.cusEmail = (cusEmail != null) ? cusEmail.toUpperCase() : null;
	}

}
