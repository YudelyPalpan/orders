package com.distributedsystems.orders.processing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.distributedsystems.orders.processing.entities.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer>{

}
