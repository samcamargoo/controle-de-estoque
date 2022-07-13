package com.sam.estoque.services;

import java.util.List;

import com.sam.estoque.dtos.ProductDto;
import com.sam.estoque.entities.Stock;

public interface StockService {

	List<ProductDto> productsUnderXUnits(int qntity);
	Stock verifyStockItemsQuantityAndTotalValue() throws Exception;
	void verifyExpirationDate() throws Exception;
}
