package com.distributedsystems.orders.warehouse.services;

public interface WarehouseService {
  void sendOrderToStockChecking(int orderId);
  
  void checkOrderItemsStock(int orderId);
}