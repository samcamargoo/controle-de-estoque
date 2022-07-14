package com.sam.estoque.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stock implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private Integer productsQuantity;
	private BigDecimal totalValue;
	private Integer expiredProductsQuantity;
	private BigDecimal expiredProductsValue;

	
}
