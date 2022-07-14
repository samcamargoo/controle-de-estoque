package com.sam.estoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sam.estoque.entities.SaleOff;

public interface SaleOffRepository extends JpaRepository<SaleOff, Long> {

	@Query("SELECT s FROM SaleOff s WHERE s.endSaleDay < CURRENT_DATE AND s.active = true")
	List<SaleOff> findExpiredSales();
	
	@Query("SELECT s from SaleOff s WHERE s.product = :id AND s.active = true")
	Optional<SaleOff> findByIdAndActive(@Param("id") Long id);
	
	@Query("SELECT s from SaleOff s join s.product p WHERE p.id = :id AND s.active = true")
	Optional<SaleOff> findProductOnSale(@Param("id") Long id);
}
