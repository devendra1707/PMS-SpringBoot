package com.pms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {

	@Id
	private int roleId;

	@Column(name = "role_name")
	private String roleName;

}
