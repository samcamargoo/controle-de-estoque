package com.sam.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sam.estoque.entities.SaleOff;

public interface SaleOffRepository extends JpaRepository<SaleOff, Long> {

}
