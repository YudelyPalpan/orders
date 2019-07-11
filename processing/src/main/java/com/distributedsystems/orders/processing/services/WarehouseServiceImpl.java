package com.distributedsystems.orders.processing.services;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private Queue warehouseQueue;

  @Value("${queue.warehouse.exchange}")
  private String warehouseExchange;

  @Override
  public void sendOrderToStockChecking(int orderId) {
    Message body = new Message(String.valueOf(orderId).getBytes(), new MessageProperties());
    rabbitTemplate.send(warehouseExchange, warehouseQueue.getName(), body);
  }
}