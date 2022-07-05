package com.sam.estoque.services;

import org.springframework.http.ResponseEntity;

public interface SalesOffService {

	ResponseEntity<Object> createSale(Long id, int percentage, int days);
}
