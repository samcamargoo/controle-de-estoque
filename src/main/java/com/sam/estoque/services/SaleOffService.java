package com.sam.estoque.services;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;

public interface SaleOffService {

	ResponseEntity<Object> createSale(Long id, int percentage, int days);
	boolean isOnSale(Long id);
	BigDecimal calculateDiscount(BigDecimal price, int percentage);
}
