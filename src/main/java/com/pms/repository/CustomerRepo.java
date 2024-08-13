package com.pms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
	Optional<Customer> findByCusEmailIgnoreCase(String email);

	Optional<Customer> findByMobNum(String mobNum);

	boolean existsByCusEmailIgnoreCase(String email);

	boolean existsByMobNum(String mobNum);
}
