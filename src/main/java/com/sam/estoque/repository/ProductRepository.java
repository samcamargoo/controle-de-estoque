package com.sam.estoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sam.estoque.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	boolean existsByBarCode(String barCode);
	
	@Query("SELECT p from Product p WHERE p.quantity <= :qntity AND p.onStock = true ORDER BY p.name")
	List<Product> findAllProductsUnderXQuantity(@Param("qntity") int qntity);

	@Query("SELECT p from Product p WHERE p.onStock = true AND p.expired = false AND p.expirationDate < CURRENT_DATE")
	List<Product> findExpiredProductsOnStock();
	
	@Query("SELECT p from Product p WHERE p.id = :id AND p.onStock = true AND p.expired = false")
	Optional<Product> saleItems(@Param("id") Long id);
	
	@Query("SELECT p from Product p WHERE p.onSale = true AND p.endSaleDay < CURRENT_DATE")
	List<Product> findExpiredSales();
}
