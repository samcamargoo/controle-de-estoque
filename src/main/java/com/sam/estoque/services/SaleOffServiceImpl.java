package com.sam.estoque.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sam.estoque.dtos.SaleOffDto;
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
		
		Optional<Product> productOptional = productRepository.findById(id);
		
		if(productOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Product not found on stock");
		}
		
		if(productOptional.get().isExpired()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Product is expired");
		}
		
		if(!productOptional.get().isOnStock()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Product is out of stock");
		}
		
		if(productOptional.get().isOnSale()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Product is already on sale");
		}
		
		
		var product = new Product();
		BeanUtils.copyProperties(productOptional.get(), product);
		product.setOnSale(true);
		productRepository.save(product);
		
		var saleOff = new SaleOff();
		saleOff.setProduct(productOptional.get());
		saleOff.setPercentage(percentage);
		saleOff.setActive(true);
		saleOff.setEndSaleDay(LocalDate.now().plusDays(days));
		saleOff.setSalePrice(calculateDiscount(productOptional.get().getSellingPrice(), percentage));
		saleOff.setCreatedAt(LocalDate.now());
		saleOffRepository.save(saleOff);
		log.info("Sale created with {}% discount and will last until {}.", percentage, saleOff.getEndSaleDay());
		
		var saleOffDto = new SaleOffDto(saleOff);
		return ResponseEntity.status(HttpStatus.CREATED).body(saleOffDto);
	}



	@Override
	public BigDecimal calculateDiscount(BigDecimal price, int percentage) {
		
		BigDecimal discount = price.multiply(BigDecimal.valueOf(percentage).divide(BigDecimal.valueOf(100)));
		BigDecimal finalPrice = price.subtract(discount).setScale(2, RoundingMode.HALF_EVEN);
		
		return finalPrice;
	}


	//Metodo executado todos os dias as 06:00 alterando promoçao vencida para inativa;
	@Override
	@Transactional
	@Scheduled(cron = " 0 0 6 * * *", zone = "America/Sao_Paulo")
	public void removeExpiredSales() {
	
		log.info("Checking expired sales");
		
		List<SaleOff> expiredSales = saleOffRepository.findExpiredSales();
		
		if(expiredSales.size() < 1) {
			log.info("No expired sales found");
			return;
		}
		
			
		for(SaleOff sale : expiredSales) {
				
				var product = new Product();
				Optional<Product> productOptional = productRepository.findById(sale.getProduct().getId());
				BeanUtils.copyProperties(productOptional.get(), product);
				product.setOnSale(false);
				productRepository.save(product);
				
				sale.setActive(false);
				saleOffRepository.save(sale);
				
		}
		
	
		log.info("{} expired sale(s) removed", expiredSales.size());
	}

}
