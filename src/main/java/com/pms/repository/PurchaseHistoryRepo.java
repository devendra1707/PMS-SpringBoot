package com.pms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pms.model.PurchaseHistory;

public interface PurchaseHistoryRepo extends JpaRepository<PurchaseHistory, Integer> {

	
//	@Query("SELECT p FROM PurchaseHistory p JOIN FETCH p.product WHERE p.id = :purchaseId")
//    Optional<PurchaseHistory> findByIdWithProduct(@Param("purchaseId") int purchaseId);
	
//	@Query("SELECT p FROM PurchaseHistory p JOIN FETCH p.product WHERE p.id = :purchaseId")
//	Optional<PurchaseHistory> findByIdWithProduct(@Param("purchaseId") int purchaseId);
	@Query("SELECT p FROM PurchaseHistory p JOIN FETCH p.product WHERE p.id = :purchaseId")
	Optional<PurchaseHistory> findByIdWithProduct(@Param("purchaseId") int purchaseId);

}
