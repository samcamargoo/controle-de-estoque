package com.sam.estoque.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sam.estoque.dtos.ProductDto;
import com.sam.estoque.entities.Product;
import com.sam.estoque.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Override
	public List<ProductDto> listAll() {
		List<Product> list = productRepository.findAll();
		return list.stream().map(x-> new ProductDto(x)).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ResponseEntity<Object> postProduct(ProductDto productDto) {
		
		if(existsByBarCode(productDto.getBarCode())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Another product is already registered with the same bar code");
		}
		
		var product = new Product();
		BeanUtils.copyProperties(productDto, product);
		return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
	}

	@Override
	public boolean existsByBarCode(String barCode) {
		
		if(!productRepository.existsByBarCode(barCode)) {
			return false;
		}
		return true;
	}
	
	

}
