package com.sam.estoque.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sam.estoque.dtos.ProductDto;
import com.sam.estoque.entities.Stock;

public interface StockService {

	List<ProductDto> productsUnderFiveUnits(int qntity);
	Stock verifyStockItemsQuantityAndTotalValue() throws Exception;
	List<ProductDto> verifyExpirationDate() throws Exception;
	ResponseEntity<Object> saleOff(Long id, int percentage);
}
