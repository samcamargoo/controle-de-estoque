package com.sam.estoque.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true)
	private String barCode;
	@Column(nullable = false)
	private String name;
	private Integer quantity;
	@Column(length = 255)
	private String description;
	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	private LocalDate expirationDate;
	private boolean expired;
	private LocalDate acquisitionDate;
	@Column(nullable = false)
	private BigDecimal acquisitionPrice;
	private BigDecimal sellingPrice;
	private boolean onStock;
	private boolean onSale;
	@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
	private LocalDate endSaleDay;
	private BigDecimal salePrice;
	
	
}
