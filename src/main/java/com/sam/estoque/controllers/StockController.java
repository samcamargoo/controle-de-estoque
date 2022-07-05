package com.sam.estoque.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sam.estoque.dtos.ProductDto;
import com.sam.estoque.entities.Stock;
import com.sam.estoque.services.StockService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/stock")
@AllArgsConstructor
public class StockController {

	private final StockService checkStockService;
	
	@GetMapping("/check-product-quantity")
	public List<ProductDto> productsUnderFiveUnits(@RequestParam int qntity) {
		return checkStockService.productsUnderFiveUnits(qntity);
	}
	
	@GetMapping("/total-value")
	public Stock verifyItemsQuantityAndTotalValue() throws Exception {
		return checkStockService.verifyStockItemsQuantityAndTotalValue();
	}
	
	@GetMapping("/verify-expiration-date")
	public List<ProductDto> verifyExpirationDate() throws Exception {
		return checkStockService.verifyExpirationDate();
	}
	
	@PostMapping("/sale-off/{id}")
	public ResponseEntity<Object> createSale(@PathVariable (value="id") Long id, @RequestParam int percentage) {
		return checkStockService.saleOff(id, percentage);
	}
}
