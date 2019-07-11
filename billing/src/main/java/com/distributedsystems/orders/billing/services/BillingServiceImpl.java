package com.distributedsystems.orders.billing.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.distributedsystems.orders.billing.components.BillingConsumer;
import com.distributedsystems.orders.billing.entities.Order;
import com.distributedsystems.orders.billing.entities.OrderItem;
import com.distributedsystems.orders.billing.entities.Product;
import com.distributedsystems.orders.billing.repositories.OrdersRepository;
import com.distributedsystems.orders.billing.repositories.ProductsRepository;

@Service("billingService")
public class BillingServiceImpl implements BillingService {
  private static final Log LOG = LogFactory.getLog(BillingServiceImpl.class);
  private static final String DEFAULT_EXCHANGE = "";
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private OrdersRepository ordersRepository;
  
  @Autowired
  private Queue financeQueue;


  @Override
  public void sendOrderToFinance(int orderId) {
    Message body = new Message(String.valueOf(orderId).getBytes(), new MessageProperties());
    rabbitTemplate.send(DEFAULT_EXCHANGE, financeQueue.getName(), body);
  }

	@Override
	public void billOrder(int orderId) {
		Optional<Order> order = ordersRepository.findById(orderId);
		if(order.isPresent()) {
			LOG.info("[Billing] Make bill for order'" + orderId + "'... ");
			LOG.info("[Billing] Bill for order '" + orderId + "'complete ");
			sendOrderToFinance(orderId);
		}
	}
	
}