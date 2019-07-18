package com.distributedsystems.orders.billing.services;

public interface BillingService {
  void sendOrderToFinance(int orderId, byte[] orderPDF);
  
  void billOrder(int orderId);
  
  
}