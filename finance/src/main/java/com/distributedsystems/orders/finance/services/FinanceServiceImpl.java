package com.distributedsystems.orders.finance.services;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distributedsystems.orders.finance.components.FinanceConsumer;
import com.distributedsystems.orders.finance.entities.Order;
import com.distributedsystems.orders.finance.repositories.OrdersRepository;

@Service("financeService")
public class FinanceServiceImpl implements FinanceService {
  private static final Log LOG = LogFactory.getLog(FinanceConsumer.class);
  
  @Autowired
  private OrdersRepository ordersRepository;
  
  @Override
  public void makeFinanceProcessing(int orderId) {
	  Optional<Order> order = ordersRepository.findById(orderId);
	
	  if(order.isPresent()) {
		LOG.info("[Finances] Starting finance process for order'" + orderId + "'... ");
		LOG.info("[Finances] Execute accounting cheking for order'" + orderId + "'... ");
		LOG.info("[Finances] Declaring bill for order '" + orderId + "' ... ");
		LOG.info("[Finances] Sending invoice for order '" + orderId + "'... ");
		LOG.info("[Finances] Finance process for order'" + orderId + "'complete ");
	  }
  }
}