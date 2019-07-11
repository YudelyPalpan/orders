package com.distributedsystems.orders.billing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distributedsystems.orders.billing.entities.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
