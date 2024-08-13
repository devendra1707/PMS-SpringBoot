package com.pms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pms.exception.CustomerAlreadyRegisteredException;
import com.pms.exception.CustomerNotFoundException;
import com.pms.exception.ResourceNotFoundException;
import com.pms.model.Customer;
import com.pms.repository.CustomerRepo;

@Service
public class CustomerService {

	private CustomerRepo customerRepo;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public CustomerService(CustomerRepo customerRepo) {
		super();
		this.customerRepo = customerRepo;
	}

	// Add Customer

	public Customer createCustomer(Customer customer) {
		// Check if mobile number or email is already registered

		if (customerRepo.existsByCusEmailIgnoreCase(customer.getCusEmail())) {
			throw new CustomerAlreadyRegisteredException("Email is already registered.");
		}
		if (customerRepo.existsByMobNum(customer.getMobNum())) {
			throw new CustomerAlreadyRegisteredException("Mobile number is already registered.");
		}

		customer.setRegDate(new Date());

		try {
			customer.setRegDate(sdf.parse(sdf.format(new Date())));
		} catch (ParseException e) {
			throw new RuntimeException("Error setting registration date", e);
		}

		return customerRepo.save(customer);
	}

	// Update Customer

	public Customer updateCustomer(int id, Customer customer) {
		Customer cusDb = customerRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		if (cusDb != null) {
			cusDb.setAddress(customer.getAddress());
			cusDb.setCusEmail(customer.getCusEmail());
			cusDb.setCusName(customer.getCusName());
			cusDb.setMobNum(customer.getMobNum());
		}
		return customerRepo.save(cusDb);
	}

	// All Customer

	public List<Customer> allCustomer() {
		List<Customer> allCus = customerRepo.findAll();
		return allCus;
	}

	// One Customer

	public Customer getCustomer(int id) {
		Customer cusDb = customerRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		return cusDb;
	}

	// find By MobNum

	public Customer findCusByMobNumber(String mobNum) {
		return customerRepo.findByMobNum(mobNum)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with mobile number: " + mobNum));
	}

	// find By Email

	public Customer findCusByEmail(String email) {
		return customerRepo.findByCusEmailIgnoreCase(email)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with Email ID: " + email));
	}
	// Delete Customer

	public void deleteCustomer(int id) {
		Customer cusDb = customerRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
		customerRepo.delete(cusDb);
	}
}
