package com.sam.estoque.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sam.estoque.services.SalesOffService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/sales-off")
@AllArgsConstructor
public class SaleOffController {
	
	private final SalesOffService salesOffService;
	
	@PostMapping("/{id}")
	public ResponseEntity<Object> createSale(@PathVariable (value = "id") Long id, @RequestParam( value= "percentage") int percentage, @RequestParam (value = "days") int days) {
		return salesOffService.createSale(id, percentage, days);
	}

}
