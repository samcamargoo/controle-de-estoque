package com.sam.estoque.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sam.estoque.entities.Product;
import com.sam.estoque.entities.SaleOff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleOffDto {


		private ProductDto productDto;
		private int percentage;
		private BigDecimal offPrice;
		@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
		private LocalDate endSaleDay;
		
		public SaleOffDto(SaleOff saleOff) {
			this.productDto = new ProductDto(saleOff.getProduct());
			this.percentage = saleOff.getPercentage();
			this.offPrice = saleOff.getOffPrice();
			this.endSaleDay = saleOff.getEndSaleDay();
		}
}
