package com.distributedsystems.orders.finance.services;

import com.distributedsystems.orders.finance.util.MessageAttachment;

public interface FinanceService {
  
  void makeFinanceProcessing(int orderId, MessageAttachment orderFile);
}