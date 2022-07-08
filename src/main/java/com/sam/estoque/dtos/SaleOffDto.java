package com.sam.estoque.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sam.estoque.entities.SaleOff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOffDto {

	private Long id;
	private ProductDto productDto;
	@NotBlank
	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	private LocalDate endSaleDay;
	@NotNull
	private int percentage;
	@NotNull
	private BigDecimal salePrice;
	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	private LocalDate createdAt;
	
	public SaleOffDto(SaleOff saleOff) {
		
		this.id = saleOff.getId();
		this.productDto = new ProductDto(saleOff.getProduct());
		this.endSaleDay = saleOff.getEndSaleDay();
		this.percentage = saleOff.getPercentage();
		this.salePrice = saleOff.getSalePrice();
		this.createdAt = saleOff.getCreatedAt();
	}
}
