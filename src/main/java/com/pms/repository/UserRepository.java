package com.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmailIgnoreCase(String email);
	
	boolean existsByEmail(String email);

}
