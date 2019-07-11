package com.distributedsystems.orders.finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distributedsystems.orders.finance.entities.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
