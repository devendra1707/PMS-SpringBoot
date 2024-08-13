package com.pms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.exception.CustomerNotFound;
import com.pms.exception.CustomerNotFoundException;
import com.pms.exception.ResourceNotFoundException;
import com.pms.model.Customer;
import com.pms.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	private CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	// Add Customer
	@PostMapping("/create")
	public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
		System.out.println("Customer before save: " + customer);
		Customer createdCustomer = customerService.createCustomer(customer);
//	    return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
		return new ResponseEntity<Customer>(createdCustomer, HttpStatus.CREATED);
	}

	// Update Customer
	@PutMapping("/update/{cusId}")
	public ResponseEntity<?> updateCustomer(@PathVariable("cusId") int cusId, @Valid @RequestBody Customer customer) {
		try {
			Customer cus = customerService.updateCustomer(cusId, customer);
			return new ResponseEntity<Customer>(cus, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Customer Not Found with this " + cusId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	// Get All Customer
	@GetMapping("/all")
	public ResponseEntity<List<Customer>> allCustomer() {
		List<Customer> cusList = customerService.allCustomer();
		return new ResponseEntity<List<Customer>>(cusList, HttpStatus.OK);
	}

	// Find one Customer
	@GetMapping("/{cusId}")
	public ResponseEntity<?> oneCustomer(@PathVariable("cusId") int cusId) {
		try {
			Customer customer = customerService.getCustomer(cusId);
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} catch (CustomerNotFound e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "customer Not Found with this ID: " + cusId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	// get Customer By Email Id

	@GetMapping("/findByEmail/{email}")
	public ResponseEntity<?> findCustomerByEmail(@PathVariable("email") String email) {
		try {
			Customer customer = customerService.findCusByEmail(email);
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} catch (CustomerNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Customer Not Found with Email ID: " + email);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	// Get Customer by Mob Number

	@GetMapping("/findByMobNum/{mobNum}")
	public ResponseEntity<?> findCustomerByMobNum(@PathVariable("mobNum") String mobNum) {
		try {
			Customer customer = customerService.findCusByMobNumber(mobNum);
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} catch (CustomerNotFoundException e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Customer Not Found with Mobile Number: " + mobNum);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	// Delete Customer
	@DeleteMapping("/delete/{cusId}")
	public ResponseEntity<?> deleteCustomer(@PathVariable("cusId") int cusId) {
		try {
			customerService.deleteCustomer(cusId);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Customer successfully deleted with ID: " + cusId);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (CustomerNotFound e) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "Customer Not Found with this ID: " + cusId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

}
