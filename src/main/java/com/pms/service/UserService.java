package com.pms.service;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pms.constants.AppConstants;
import com.pms.exception.InvalidRoleIdException;
import com.pms.exception.ResourceNotFoundException;
import com.pms.exception.SingleRegistrationException;
import com.pms.model.Role;
import com.pms.model.User;
import com.pms.repository.RoleRepo;
import com.pms.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private UserRepository userRepo;

	public User registerNewUser(User user, int rId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Set registration date if not already set
		if (user.getRegDate() == null) {
			try {
				user.setRegDate(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				throw new RuntimeException("Error setting registration date", e);
			}
		}

		// Check if the user is already registered
		if (userRepo.existsByEmail(user.getEmail())) {
			throw new SingleRegistrationException(user.getEmail() + " :: User is already registered, Please login.");
		}

		// Encode the user's password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		// Check role ID and assign role accordingly
		if (rId == AppConstants.NORMAL_USER || rId == AppConstants.ADMIN_USER) {
			Role role = this.roleRepo.findById(rId).orElseThrow(() -> new InvalidRoleIdException("Invalid role ID."));
			user.getRoles().add(role);

			// Save the new user and return it
			return this.userRepo.save(user);
		} else {
			throw new InvalidRoleIdException("Invalid role ID.");
		}
	}

	public Iterable<User> getAll() {
		return userRepo.findAll();
	}

	// delete user
	public void deleteUser(int id) {
		User user = userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
		userRepo.delete(user);
	}

//	// get all vendors created by admin
//	public Set<Vendors> getAllVendorsByAdmin(int adminId) {
//		User admin = userRepo.findById(adminId)
//				.orElseThrow(() -> new ResourceNotFoundException("Admin not found with id " + adminId));
//		return admin.getVendors();
//	}
//
//	// get all employees created by admin
//	public Set<Employees> getAllEmployeesByAdmin(int adminId) {
//		User admin = userRepo.findById(adminId)
//				.orElseThrow(() -> new ResourceNotFoundException("Admin not found with id " + adminId));
//		return admin.getEmployees();
//	}
//
//	// create and save vendor
//	public Vendors createVendor(Vendors vendor, int adminId) {
//		User admin = userRepo.findById(adminId)
//				.orElseThrow(() -> new ResourceNotFoundException("Admin not found with id " + adminId));
//		vendor.setCreatedBy(admin);
//		return vendorsRepo.save(vendor);
//	}
//
//	// create and save employee
//	public Employees createEmployee(Employees employee, int adminId) {
//		User admin = userRepo.findById(adminId)
//				.orElseThrow(() -> new ResourceNotFoundException("Admin not found with id " + adminId));
//		employee.setCreatedBy(admin);
//		return employeesRepo.save(employee);
//	}

//	public User getSingleData(String email) {
//		return userRepository.findByEmailIgnoreCase(email);
//	}

//	public User create(User user) {
//		// Encrypt the user's password
//		String password = user.getPassword();
//		String encrypt = bCryptPasswordEncoder.encode(password);
//		user.setPassword(encrypt);
//		user.setDate(new Date());
//
//		// Set roles for the user
//		Set<Role> roles = new HashSet<>();
//		Role role = new Role();
//		role.setRoleName("ADMIN");
//		roles.add(role);
//		user.setRoles(roles);
//
//		return userRepo.save(user);
//	}

	// get current user

	public User getCurrentUser(Principal principal) {
		if (principal == null) {
			System.out.println("Principal is null");
			return null;
		}

		String username = principal.getName();
//		System.out.println("Email Id -------------" + username);

		return userRepo.findByEmailIgnoreCase(username);
	}

}
