package com.sam.estoque.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
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

	private final StockService stockService;
	
	@GetMapping("/check-product-quantity")
	public List<ProductDto> productsUnderXUnits(@RequestParam int qntity) {
		return stockService.productsUnderXUnits(qntity);
	}
	
	@GetMapping("/total-value")
	public Stock verifyItemsQuantityAndTotalValue() throws Exception {
		return stockService.verifyStockItemsQuantityAndTotalValue();
	}
	
	@GetMapping("/verify-expiration-date")
	public void verifyExpirationDate() throws Exception {
		stockService.verifyExpirationDate();
	}
}
