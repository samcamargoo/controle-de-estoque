package com.sam.estoque.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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
	

	// Metodo que encontra todos os produtos com estoque abaixo da quantidade
	// informada
	@Override
	public List<ProductDto> productsUnderXUnits(int qntity) {

		List<Product> products = productRepository.findAllProductsUnderXQuantity(qntity);
		return products.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());

	}

	/*
	 * Metodo que verifica a quantidade de items no estoque e o seu valor total,
	 *  verificando tambem a quantidade de items vencidos e o prejuizo com os items vencidos.
	 */

	@Override
	public Stock verifyStockItemsQuantityAndTotalValue() throws Exception {

		verifyExpirationDate();
		List<Product> products = productRepository.findAllOnStock();

		Integer quantity = 0;
		BigDecimal totalValue = BigDecimal.ZERO;
		
		Integer expiredProductsQuantity = 0;
		BigDecimal expiredProductsTotalValue = BigDecimal.ZERO;

		if (products.size() < 1) {
			return new Stock(quantity, totalValue, expiredProductsQuantity,
					expiredProductsTotalValue);
		}

		for (Product p : products) {
		
			if (!p.isExpired()) {
				quantity += p.getQuantity();
				totalValue = totalValue.add(p.getAcquisitionPrice().multiply(BigDecimal.valueOf(p.getQuantity())));
				System.out.println(totalValue);
			} else {
				expiredProductsQuantity += p.getQuantity();
				expiredProductsTotalValue = expiredProductsTotalValue.add(p.getAcquisitionPrice().multiply(BigDecimal.valueOf(p.getQuantity())));
			}
			
		}
		return new Stock(quantity, totalValue, expiredProductsQuantity, expiredProductsTotalValue);
	}

	// Metodo executado todos os dias as 06:00 para verificar produtos vencidos no estoque. 
	@Override
	@Scheduled(cron = "0 0 6 * * *", zone = "America/Sao_Paulo")
	@Transactional
	public void verifyExpirationDate() throws Exception {

		List<Product> expiredProducts = productRepository.findExpiredProductsOnStock();

		if (expiredProducts.size() > 0) {
			try {

				for (Product product : expiredProducts) {

					product.setExpired(true);
					productRepository.save(product);

				}
			} catch (Exception e) {
				throw new Exception("Error");
			}
		}
		log.info(expiredProducts.size() + " product(s) expired on stock");
	}

}
