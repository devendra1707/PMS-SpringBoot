package com.pms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pms.config.CustomDateSerializer;

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
public class PurchageProducts {

	private int quantityPurchased;

	@JsonSerialize(using = CustomDateSerializer.class)
	private Date purchaseDate;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Products product;

//	private Customer customer;

}
