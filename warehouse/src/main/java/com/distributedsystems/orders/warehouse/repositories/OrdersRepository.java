package com.distributedsystems.orders.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.distributedsystems.orders.warehouse.entities.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer>{

}
