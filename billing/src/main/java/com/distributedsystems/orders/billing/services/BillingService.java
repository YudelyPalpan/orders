package com.distributedsystems.orders.billing.services;

public interface BillingService {
  void sendOrderToFinance(int orderId);
  
  void billOrder(int orderId);
}