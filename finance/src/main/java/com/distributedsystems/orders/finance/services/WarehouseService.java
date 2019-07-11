package com.distributedsystems.orders.finance.services;

public interface WarehouseService {
  void sendOrderToStockChecking(int orderId);
  
  void checkOrderItemsStock(int orderId);
}