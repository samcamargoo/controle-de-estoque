package com.sam.estoque.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sam.estoque.services.SaleOffService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/sales")
@AllArgsConstructor
public class SaleOffController {
	
	private final SaleOffService saleOffService;
	
	@PostMapping("/{id}")
	public ResponseEntity<Object> createSale(@PathVariable (name="id") Long id, @RequestParam int percentage, @RequestParam int days) {
		return saleOffService.createSale(id, percentage, days);
	}

}
