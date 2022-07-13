package com.sam.estoque.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stock implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private Long itemsQuantity;
	private BigDecimal totalValue;
	private Long itemsOnSaleQuantity;
	private BigDecimal itemsOnSaleValue;
	private Long expiredProductsQuantity;
	private BigDecimal expiredProductsValue;

	
}
