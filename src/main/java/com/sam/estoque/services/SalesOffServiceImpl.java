package com.sam.estoque.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sam.estoque.dtos.SaleOffDto;
import com.sam.estoque.entities.Product;
import com.sam.estoque.entities.SaleOff;
import com.sam.estoque.repository.ProductRepository;
import com.sam.estoque.repository.SaleOffRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SalesOffServiceImpl implements SalesOffService {

	private final ProductRepository productRepository;
	private final SaleOffRepository saleOffRepository;
	
	
	
	/*Metodo para criar uma promoçao, verifica se o produto com Id informado existe no banco de dados,
	 verifica se o produto tem unidades disponiveis no estoque,
	 calcula os descontos com a porcentagem e datas informadas pelo usuario e finalmente salva a promoçao;
	*/
	@Override
	@Transactional
	public ResponseEntity<Object> createSale(Long id, int percentage, int days) {
		
		Optional<Product> productOptional = productRepository.findById(id);
		
		if(productOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
		}
		
		if(!productOptional.get().isOnStock()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Product is out of stock"); 
		}
		
		//Calculo de desconto
		BigDecimal price = productOptional.get().getSellingPrice();
		BigDecimal discount = price.multiply(BigDecimal.valueOf(percentage).divide(BigDecimal.valueOf(100)));
		BigDecimal finalPrice = price.subtract(discount).setScale(2, RoundingMode.HALF_EVEN);


		var saleOff = new SaleOff();
		saleOff.setProduct(productOptional.get());
		saleOff.setPercentage(percentage);
		saleOff.setEndSaleDay(LocalDate.now().plusDays(days));
		saleOff.setOffPrice(finalPrice);
		saleOffRepository.save(saleOff);
		var saleOffDto = new SaleOffDto(saleOff);
		return ResponseEntity.status(HttpStatus.CREATED).body(saleOffDto);
		
	}

}
