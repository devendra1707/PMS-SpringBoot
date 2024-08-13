package com.pms.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int prodId;

	@NotNull(message = "Product Name is required")
	@NotBlank(message = "Product Name can not be blank")
	@Length(min = 3, max = 50, message = "Product Name Must be 3 to 50 character.")
	private String productName;

	@Min(value = 0, message = "Quantity must be at least 0")
	@Max(value = 10000, message = "Quantity must be at most 10,000")
	private int productQuantity;

	private double productPrice;

	private double productDiscount;

	@NotNull(message = "Description is required")
	@NotBlank(message = "Description can not be blank")
	@Length(min = 5, max = 500, message = "Description Must be 5 to 500 character.")
	private String description;

//	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonManagedReference
//	private Set<PurchaseHistory> purchaseHistory = new HashSet<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference
	private Set<PurchaseHistory> purchaseHistory = new HashSet<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<ProductAddHistory> addHistory = new HashSet<>();

	@ManyToOne
	@JsonIgnore
//	@JsonBackReference
	private User user;
}
