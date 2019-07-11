package com.distributedsystems.orders.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distributedsystems.orders.warehouse.entities.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
