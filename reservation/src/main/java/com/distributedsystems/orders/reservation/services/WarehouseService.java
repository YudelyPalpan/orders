package com.distributedsystems.orders.reservation.services;

public interface WarehouseService {
  void sendOrderToStockChecking(int orderId);
  
  void checkOrderItemsStock(int orderId);
}