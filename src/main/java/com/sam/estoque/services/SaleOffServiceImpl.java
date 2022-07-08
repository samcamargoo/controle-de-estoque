package com.sam.estoque.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sam.estoque.entities.Product;
import com.sam.estoque.entities.SaleOff;
import com.sam.estoque.repository.ProductRepository;
import com.sam.estoque.repository.SaleOffRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SaleOffServiceImpl implements SaleOffService{

	private final ProductRepository productRepository;
	private final SaleOffRepository saleOffRepository;
	
	//Metodo que cria uma promoção baseada na porcentagem e data informada pelo usuário.
	@Override
	@Transactional
	public ResponseEntity<Object> createSale(Long id, int percentage, int days) {
		
		if(isOnSale(id)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Product is on sale already");
		}
		
		Optional<Product> productOptional = productRepository.findById(id);
		
		var product = new Product();
		BeanUtils.copyProperties(productOptional.get(), product);
		product.setOnSale(true);
		productRepository.save(product);
		
		var saleOff = new SaleOff();
		saleOff.setProduct(productOptional.get());
		saleOff.setPercentage(percentage);
		saleOff.setEndSaleDay(LocalDate.now().plusDays(days));
		saleOff.setSalePrice(calculateDiscount(productOptional.get().getSellingPrice(), percentage));
		saleOff.setCreatedAt(LocalDate.now());
		log.info("Sale created with {}% discount and will last until {}.", percentage, saleOff.getEndSaleDay());
		return ResponseEntity.status(HttpStatus.CREATED).body(saleOffRepository.save(saleOff));
	}

	@Override
	public boolean isOnSale(Long id) {
		
		if(!productRepository.isOnSale(id)) {
			return false;
		}
		return true;
	}

	@Override
	public BigDecimal calculateDiscount(BigDecimal price, int percentage) {
		
		BigDecimal discount = price.multiply(BigDecimal.valueOf(percentage).divide(BigDecimal.valueOf(100)));
		BigDecimal finalPrice = price.subtract(discount).setScale(2, RoundingMode.HALF_EVEN);
		
		return finalPrice;
	}

}
