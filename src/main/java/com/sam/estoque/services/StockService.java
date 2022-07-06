package com.sam.estoque.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sam.estoque.dtos.ProductDto;
import com.sam.estoque.entities.Stock;

public interface StockService {

	List<ProductDto> productsUnderXUnits(int qntity);
	Stock verifyStockItemsQuantityAndTotalValue() throws Exception;
	void verifyExpirationDate() throws Exception;
	ResponseEntity<Object> createSale(Long id, int percentage, int days);
	void removeExpiredSales();
}
