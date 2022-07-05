package com.sam.estoque.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.sam.estoque.dtos.ProductDto;

public interface ProductService {

	List<ProductDto> listAll();
	ResponseEntity<Object> postProduct(ProductDto productDto);
	boolean existsByBarCode(String barCode);
}
