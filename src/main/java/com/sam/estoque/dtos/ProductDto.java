package com.sam.estoque.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sam.estoque.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {


	private Long id;
	@NotBlank
	private String barCode;
	@NotBlank
	private String name;
	private Integer quantity;
	private String description;
	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	private LocalDate expirationDate;
	private boolean expired;
	private LocalDate acquisitionDate;
	@NotNull
	private BigDecimal acquisitionPrice;
	@NotNull
	private BigDecimal sellingPrice;
	private boolean onStock;
	private boolean onSale;
	
	public ProductDto(Product product) {
		this.id = product.getId();
		this.barCode = product.getBarCode();
		this.name = product.getName();
		this.quantity = product.getQuantity();
		this.description = product.getDescription();
		this.expirationDate = product.getExpirationDate();
		this.expired = product.isExpired();
		this.acquisitionDate = product.getAcquisitionDate();
		this.acquisitionPrice = product.getAcquisitionPrice();
		this.sellingPrice = product.getSellingPrice();
		this.onStock = product.isOnStock();
		this.onSale = product.isOnSale();
	}
}
