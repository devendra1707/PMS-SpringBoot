package com.pms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pms.exception.InsufficientProductQuantityException;
import com.pms.exception.ProductNotFoundException;
import com.pms.exception.ResourceNotFoundException;
import com.pms.model.ProductAddHistory;
import com.pms.model.Products;
import com.pms.model.PurchaseHistory;
import com.pms.repository.ProductAddHistoryRepo;
import com.pms.repository.ProductRepo;
import com.pms.repository.PurchaseHistoryRepo;

@Service
public class ProductService {

	private ProductRepo productRepo;

	private PurchaseHistoryRepo purchaseHistoryRepo;

	private ProductAddHistoryRepo addHistoryRepo;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public ProductService(ProductRepo productRepo, PurchaseHistoryRepo purchaseHistoryRepo,
			ProductAddHistoryRepo addHistoryRepo) {
		this.productRepo = productRepo;
		this.purchaseHistoryRepo = purchaseHistoryRepo;
		this.addHistoryRepo = addHistoryRepo;
	}

//Purchase Product
	public PurchaseHistory purchaseProduct(int prodId, int quantityPurchased, String cusName, String cusEmail,
			String mobNum, String address) {
		Products product = productRepo.findById(prodId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + prodId));

		if (product.getProductQuantity() < quantityPurchased) {
			throw new InsufficientProductQuantityException(
					"Insufficient quantity available for product with id " + prodId);
		}

		product.setProductQuantity(product.getProductQuantity() - quantityPurchased);

		double prodPrice = product.getProductPrice();
		double prodDis = product.getProductDiscount();
		double discountAmount = prodPrice * prodDis / 100;
		double finalprise = prodPrice - discountAmount;

		double purPurchaseAmount = finalprise * quantityPurchased;

		// Calculate total amount
//		double totalAmount = product.getProductPrice() * quantityPurchased;

		// Save the purchase history
		PurchaseHistory purchaseHistory = new PurchaseHistory();

		purchaseHistory.setQuantityPurchased(quantityPurchased);
		purchaseHistory.setTotalAmount(String.format("%.2f", purPurchaseAmount));
		// Customer Details
		purchaseHistory.setCusName(cusName);
		purchaseHistory.setCusEmail(cusEmail);
		purchaseHistory.setMobNum(mobNum);
		purchaseHistory.setAddress(address);

		// Product Details

		purchaseHistory.setDescription(product.getDescription());
		purchaseHistory.setProductDiscount(product.getProductDiscount());
		purchaseHistory.setProductPrice(product.getProductPrice());
		purchaseHistory.setProductName(product.getProductName());

		try {
			purchaseHistory.setPurchaseDate(sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
			throw new RuntimeException("Error setting purchase date", e);
		}
		purchaseHistory.setProduct(product);

		purchaseHistoryRepo.save(purchaseHistory);
		productRepo.save(product);

		return purchaseHistory;
	}

	// All Purchase Details

	public List<PurchaseHistory> purchaseDetails() {
		return purchaseHistoryRepo.findAll();
	}

//	// Purchase Product
//	public PurchaseHistory purchaseProduct(int prodId, int quantityPurchased) {
//		Products product = productRepo.findById(prodId)
//				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + prodId));
//
//		if (product.getProductQuantity() < quantityPurchased) {
//			throw new InsufficientProductQuantityException(
//					"Insufficient quantity available for product with id " + prodId);
//		}
//
//		product.setProductQuantity(product.getProductQuantity() - quantityPurchased);
//
//		// Calculate total amount
//		double totalAmount = product.getProductPrice() * quantityPurchased;
//
//		// Save the purchase history
//		PurchaseHistory purchaseHistory = new PurchaseHistory();
//		purchaseHistory.setQuantityPurchased(quantityPurchased);
//		purchaseHistory.setTotalAmount(String.format("%.2f", totalAmount));
//
//		try {
//			purchaseHistory.setPurchaseDate(sdf.parse(sdf.format(new Date())));
//		} catch (ParseException e) {
//			throw new RuntimeException("Error setting purchase date", e);
//		}
//		purchaseHistory.setProduct(product);
//
//		purchaseHistoryRepo.save(purchaseHistory);
//		productRepo.save(product);
//
//		return product;
//	}

	// Add products
	public Products addProduct(int prodId, int products) {
		Products product = new Products();
		product = productRepo.findById(prodId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + prodId));

		if (product != null) {
			product.setProductQuantity(product.getProductQuantity() + products);

			ProductAddHistory addHistory = new ProductAddHistory();
			addHistory.setQuantityAdd(products);

			try {
				addHistory.setAddDate(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				throw new RuntimeException("Error setting registration date", e);
			}
			addHistory.setProduct(product);

			addHistoryRepo.save(addHistory);
			productRepo.save(product);
		}

		return product;
	}

	public List<PurchaseHistory> getPurchaseHistoryByProductId(int prodId) {
		Products product = productRepo.findById(prodId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + prodId));

		return new ArrayList<>(product.getPurchaseHistory());
	}

// Get Purchase Product
//	public PurchaseHistory getPurchaseProduct(int purchaseId) {
//		PurchaseHistory getPurchaseProd = purchaseHistoryRepo.findById(purchaseId)
//				.orElseThrow(() -> new ResourceNotFoundException("Product not Purchase with id "));
//		return getPurchaseProd;
//	}

	public PurchaseHistory getPurchaseProduct(int purchaseId) {
		return purchaseHistoryRepo.findByIdWithProduct(purchaseId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not Purchase with id " + purchaseId));
	}

	// create and save products
	public Products createProduct(Products products) {
		return productRepo.save(products);
	}

	// update products

	public Products updateProduct(int prodId, Products products) {
		Products productsdb = new Products();
		productsdb = productRepo.findById(prodId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + prodId));

		if (productsdb != null) {
			productsdb.setProductName(products.getProductName());
			productsdb.setProductPrice(products.getProductPrice());
			productsdb.setDescription(products.getDescription());
			productsdb.setProductDiscount(products.getProductDiscount());

			productRepo.save(productsdb);
		}

		return productsdb;
	}

	// In your service layer
	public Products getProduct(int prodId) throws ProductNotFoundException {
		return productRepo.findById(prodId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id " + prodId));
	}

	// get all products
	public List<Products> getAllProducts() {
		List<Products> allPro = productRepo.findAll();

		return allPro;
	}

	// Delete Product by ID
	public void deleteProduct(int prodId) {
		Products productsdb = productRepo.findById(prodId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found with id " + prodId));

		productRepo.delete(productsdb);
	}

}
