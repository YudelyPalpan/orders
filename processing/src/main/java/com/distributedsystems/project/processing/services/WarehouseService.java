package com.distributedsystems.project.processing.services;

public interface WarehouseService {
  void sendOrderToStockChecking(int orderId);
}