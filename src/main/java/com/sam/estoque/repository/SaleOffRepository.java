package com.sam.estoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sam.estoque.entities.SaleOff;

public interface SaleOffRepository extends JpaRepository<SaleOff, Long> {

	@Query("SELECT s FROM SaleOff s WHERE s.endSaleDay < CURRENT_DATE")
	List<SaleOff> findExpiredSales();
}
