package com.distributedsystems.orders.processing.services;

public interface WarehouseService {
  void sendOrderToStockChecking(int orderId);
}