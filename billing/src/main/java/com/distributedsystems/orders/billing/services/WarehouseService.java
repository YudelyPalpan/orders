package com.distributedsystems.orders.billing.services;

public interface WarehouseService {
  void sendOrderToStockChecking(int orderId);
  
  void checkOrderItemsStock(int orderId);
}