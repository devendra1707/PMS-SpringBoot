package com.pms.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pms.config.CustomDateSerializer;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PurchaseHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int quantityPurchased;
	@JsonSerialize(using = CustomDateSerializer.class)
	private Date purchaseDate;

	// Customer Details
	private String cusName;
	private String cusEmail;
	private String mobNum;
	private String address;

	// Product Details

	private String productName;
	private double productPrice;
	private double productDiscount;
	private String description;
	//

	private String totalAmount;

	public void setCusEmail(String email) {
		this.cusEmail = (email != null) ? email.toUpperCase() : null;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Products product;

	public void setId(int id) {
		this.id = id;
	}

	public void setQuantityPurchased(int quantityPurchased) {
		this.quantityPurchased = quantityPurchased;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public void setMobNum(String mobNum) {
		this.mobNum = mobNum;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public void setProductDiscount(double productDiscount) {
		this.productDiscount = productDiscount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
