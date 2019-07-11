package com.distributedsystems.project.warehouse.services;

public interface WarehouseService {
  void sendOrderToStockChecking(int orderId);
  
  void checkOrderItemsStock(int orderId);
}