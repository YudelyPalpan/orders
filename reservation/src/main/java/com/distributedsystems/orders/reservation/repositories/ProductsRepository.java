package com.distributedsystems.orders.reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distributedsystems.orders.reservation.entities.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>{

}
