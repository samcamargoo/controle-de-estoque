package com.sam.estoque.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

@Service
@AllArgsConstructor
public class StockServiceImpl implements StockService {

	private final ProductRepository productRepository;

	@Override
	public List<ProductDto> productsUnderFiveUnits(int qntity) {
		
		List<Product> products = productRepository.findAllProductsUnderXQuantity(qntity);
		return products.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());
		
	}

	//Metodo que verifica a quantidade de items no estoque e o seu valor total,
	//verificando tambem a quantidade de items vencidos e o prejuizo com os items vencidos.
	
	@Override
	public Stock verifyStockItemsQuantityAndTotalValue() throws Exception {
		
		verifyExpirationDate();
		List<Product> products = productRepository.findAll();
		List<Product> filteredProducts = products.stream()
				.filter(p -> p.getQuantity() != null && p.getAcquisitionPrice() != null && p.isOnStock() == true)
				.collect(Collectors.toList());
				
		Long quantity = 0L;
		Long expiredProductsQuantity = 0L;
		BigDecimal totalValue = BigDecimal.ZERO;
		BigDecimal expiredProductsValue = BigDecimal.ZERO;
		
		for (Product p : filteredProducts) {
			
			
			
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
	public List<ProductDto> verifyExpirationDate() throws Exception {
	
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
		
		return expiredProducts.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());
		
	}

	//Metodo que cria uma promoção baseada em uma porcentagem informada pelo usuário e modifica seus valores no banco de dados.
	@Override
	public ResponseEntity<Object> saleOff(Long id, int percentage) {
		
		Optional<Product> productOptional = productRepository.saleItems(id);
		
		if(productOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with " + id + " not found");
		}
		
		var saleProduct = new Product();
		BeanUtils.copyProperties(productOptional.get(), saleProduct);
		saleProduct.setSellingPrice(productOptional.get().getSellingPrice().subtract(BigDecimal.valueOf(percentage / 100)));
		System.out.println(saleProduct.getSellingPrice());
		return null;
	}
}
