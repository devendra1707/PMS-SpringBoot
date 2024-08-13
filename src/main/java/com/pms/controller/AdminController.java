package com.pms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService adminService;

//	@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("/vendors/{adminId}")
//	public ResponseEntity<Set<Vendors>> getAllVendorsByAdmin(@PathVariable int adminId) {
//		Set<Vendors> vendors = adminService.getAllVendorsByAdmin(adminId);
//		return ResponseEntity.ok(vendors);
//	}
//
//	@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("/employees/{adminId}")
//	public ResponseEntity<Set<Employees>> getAllEmployeesByAdmin(@PathVariable int adminId) {
//		Set<Employees> employees = adminService.getAllEmployeesByAdmin(adminId);
//		return ResponseEntity.ok(employees);
//	}
//
//	@PreAuthorize("hasRole('ADMIN')")
//	@PostMapping("/vendors/{adminId}")
//	public ResponseEntity<Vendors> createVendor(@RequestBody Vendors vendor, @PathVariable int adminId) {
//		Vendors createdVendor = adminService.createVendor(vendor, adminId);
//		return ResponseEntity.ok(createdVendor);
//	}
//
//	@PreAuthorize("hasRole('ADMIN')")
//	@PostMapping("/employees/{adminId}")
//	public ResponseEntity<Employees> createEmployee(@RequestBody Employees employee, @PathVariable int adminId) {
//		Employees createdEmployee = adminService.createEmployee(employee, adminId);
//		return ResponseEntity.ok(createdEmployee);
//	}

}
