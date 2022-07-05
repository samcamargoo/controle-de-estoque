package com.sam.estoque.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.estoque.dtos.ProductDto;
import com.sam.estoque.services.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/products")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;
	
	@GetMapping
	public List<ProductDto> listAll() {
		return productService.listAll();
	}
	
	@PostMapping
	public ResponseEntity<Object> postProduct(@Valid @RequestBody ProductDto productDto) {
		return productService.postProduct(productDto);
	}
}
