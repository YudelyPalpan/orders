package com.distributedsystems.orders.finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.distributedsystems.orders.finance.entities.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer>{

}
