package com.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.model.ProductAddHistory;

public interface ProductAddHistoryRepo extends JpaRepository<ProductAddHistory, Integer> {

}
