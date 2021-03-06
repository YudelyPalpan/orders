package com.distributedsystems.orders.billing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.distributedsystems.orders.billing.entities.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer>{

}
