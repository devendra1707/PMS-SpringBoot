package com.pms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pms.config.CustomDateSerializer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductAddHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int quantityAdd;
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date addDate;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Products product;
}
