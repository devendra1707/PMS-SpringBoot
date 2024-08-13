package com.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pms.model.Products;

@Repository
public interface ProductRepo extends JpaRepository<Products, Integer>{

}
