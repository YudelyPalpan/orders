package com.distributedsystems.orders.reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.distributedsystems.orders.reservation.entities.Order;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Integer>{

}
