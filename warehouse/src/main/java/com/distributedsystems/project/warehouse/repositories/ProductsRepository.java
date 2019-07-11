package com.distributedsystems.project.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distributedsystems.project.warehouse.entities.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
