package com.sam.estoque.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sam.estoque.dtos.ProductDto;
import com.sam.estoque.entities.Product;
import com.sam.estoque.entities.Stock;
import com.sam.estoque.repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

	
	private final ProductRepository productRepository;

	//Metodo que encontra todos os produtos com estoque abaixo da quantidade informada
	@Override
	public List<ProductDto> productsUnderXUnits(int qntity) {
		
		List<Product> products = productRepository.findAllProductsUnderXQuantity(qntity);
		return products.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());
		
	}

	
	/*Metodo que verifica a quantidade de items no estoque e o seu valor total,
	verificando tambem a quantidade de items vencidos e o prejuizo com os items vencidos.*/
	@Override
	public Stock verifyStockItemsQuantityAndTotalValue() throws Exception {
		
		verifyExpirationDate();
		List<Product> products = productRepository.findAllOnStock();
		
				
		Long quantity = 0L;
		Long expiredProductsQuantity = 0L;
		BigDecimal totalValue = BigDecimal.ZERO;
		BigDecimal expiredProductsValue = BigDecimal.ZERO;
		
		for (Product p : products) {
			
			if(!p.isExpired()) {
				quantity += p.getQuantity();
				totalValue = p.getAcquisitionPrice().multiply(BigDecimal.valueOf(quantity));
			} else {
				expiredProductsQuantity += p.getQuantity();
				expiredProductsValue = p.getAcquisitionPrice().multiply(BigDecimal.valueOf(p.getQuantity()));
			}
			
		}
		return new Stock(quantity, totalValue, expiredProductsQuantity, expiredProductsValue);
	}

	
	//Metodo que verifica validade dos produtos e os setam como vencidos.
	@Override
	@Scheduled(cron ="0 0 6 * * *", zone ="America/Sao_Paulo")
	@Transactional
	public void verifyExpirationDate() throws Exception {
	
		List<Product> expiredProducts = productRepository.findExpiredProductsOnStock();
		
		if(expiredProducts.size() > 0) {
			try {
				for(Product expired : expiredProducts) {
					
					var product = new Product();
					BeanUtils.copyProperties(expired, product);
					product.setId(expired.getId());
					product.setExpired(true);
					//product.setOnStock(false);
					productRepository.save(product);
					
				}
			} catch(Exception e) {
				throw new Exception("Error");
			}
		}	
		log.info(expiredProducts.size() + " product(s) expired on stock");
	}
	

	
	//Metodo que remove promoçoes vencidas e retorna o preço original
	@Override
	@Transactional
	@Scheduled(cron = "0 43 16 * * *", zone="America/Sao_Paulo")
	public void removeExpiredSales() {
		
		log.info("Checking for expired sales");
		
		/*List<Product> expiredSales = productRepository.findExpiredSales();
		
		if(expiredSales.size() > 0) {
			
			for(Product p : expiredSales) {
				var product = new Product();
				BeanUtils.copyProperties(p, product);
				product.setOnSale(false);
				//product.setSalePrice(0);
				productRepository.save(product);
			}
			log.info("{} sale(s) expired and removed", expiredSales.size()); */
		}	
	}

