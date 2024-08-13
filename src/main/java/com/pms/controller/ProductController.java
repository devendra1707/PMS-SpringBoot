package com.pms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pms.exception.ProductNotFoundException;
import com.pms.exception.ResourceNotFoundException;
import com.pms.model.Products;
import com.pms.model.PurchaseHistory;
import com.pms.repository.PurchaseHistoryRepo;
import com.pms.service.ProductService;
import com.pms.util.PdfService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
@Tag(name = "Product Controller", description = "Here Maitain The Incident Realated Resposbility")
public class ProductController {

	private ProductService productService;
	private PdfService pdfService;
	private PurchaseHistoryRepo purchaseHistoryRepo;

	public ProductController(ProductService productService, PdfService pdfService,
			PurchaseHistoryRepo purchaseHistoryRepo) {
		this.productService = productService;
		this.pdfService = pdfService;
		this.purchaseHistoryRepo = purchaseHistoryRepo;
	}

	// Purchage Product
	@PostMapping("/purchase/{prodId}/{quantity}")
	public ResponseEntity<PurchaseHistory> purchaseProduct(@PathVariable("prodId") int prodId,
			@PathVariable("quantity") int quantity, @RequestParam String cusName, @RequestParam String cusEmail,
			@RequestParam String mobNum, @RequestParam String address) {

		PurchaseHistory purchaseHistory = productService.purchaseProduct(prodId, quantity, cusName, cusEmail, mobNum,
				address);

		return ResponseEntity.ok(purchaseHistory);
	}

	// All Purchase History

	@GetMapping("/purchasedetails")
	public ResponseEntity<List<PurchaseHistory>> allPurchase() {
		List<PurchaseHistory> allDetails = productService.purchaseDetails();
		return new ResponseEntity<List<PurchaseHistory>>(allDetails, HttpStatus.OK);
	}

	// Generate PDF
	@GetMapping("/purchase/download/{id}/pdf")
	public ResponseEntity<byte[]> getPurchasePdf(@PathVariable("id") int id) {
		PurchaseHistory purchaseHistory = purchaseHistoryRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Purchase history not found with id " + id));
		try {
			byte[] pdfBytes = pdfService.generatePurchasePdf(purchaseHistory);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDisposition(ContentDisposition.attachment().filename("purchase-receipt.pdf").build());
			return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Add Products
	@PutMapping("/add/{prodId}/qualnity/{quantity}")
	public ResponseEntity<?> addProducts(@PathVariable int prodId, @PathVariable int quantity) {
		try {
			Products updatedProduct = productService.addProduct(prodId, quantity);
			return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/{prodId}/history")
	public ResponseEntity<?> getPurchaseHistory(@PathVariable int prodId) {
		try {
			List<PurchaseHistory> history = productService.getPurchaseHistoryByProductId(prodId);
			return new ResponseEntity<>(history, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	// create Products

	@PostMapping("/create")
	public ResponseEntity<Products> createProduct(@Valid @RequestBody Products products) {
		Products product = productService.createProduct(products);
		return new ResponseEntity<Products>(product, HttpStatus.CREATED);

	}

	// update Products

	@PutMapping("/update/{prodId}")
	public ResponseEntity<?> updateProduct(@PathVariable("prodId") int prodId, @Valid @RequestBody Products products) {
		try {
			Products product = productService.updateProduct(prodId, products);
			return new ResponseEntity<Products>(product, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Product Not Found with this " + prodId);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	// get all Products

	@GetMapping("/all")
	public ResponseEntity<List<Products>> allProduct() {
		List<Products> productList = productService.getAllProducts();
		return new ResponseEntity<>(productList, HttpStatus.OK);
	}

	// Find one Product
	@GetMapping("/{prodId}")
	public ResponseEntity<?> oneProduct(@PathVariable("prodId") int prodId) {
		try {
			Products product = productService.getProduct(prodId);
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Product Not Found with this ID: " + prodId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	// Get Purchase Product

//	@GetMapping("purchaseprod/{purchId}")
//	public ResponseEntity<?> getPurchaseProduct(@PathVariable("purchId") int purchId) {
//		try {
//			PurchaseHistory purchaseHistory = productService.getPurchaseProduct(purchId);
//			return new ResponseEntity<>(purchaseHistory, HttpStatus.OK);
//		} catch (ProductNotFoundException e) {
//			Map<String, String> response = new HashMap<>();
//			response.put("message", "Product Not Purchase with this ID: " + purchId);
//			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//		}
//	}

	// Get Purchase Product By ID
	@GetMapping("purchaseprod/{purchId}")
	public ResponseEntity<?> getPurchaseProduct(@PathVariable("purchId") int purchId) {
		try {
			PurchaseHistory purchaseHistory = productService.getPurchaseProduct(purchId);
			return new ResponseEntity<>(purchaseHistory, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Purchase history not found with ID: " + purchId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{prodId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("prodId") int prodId) {
		try {
			productService.deleteProduct(prodId);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Product successfully deleted with ID: " + prodId);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Product Not Found with this ID: " + prodId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

}
