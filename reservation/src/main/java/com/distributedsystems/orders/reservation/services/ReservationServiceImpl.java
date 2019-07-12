package com.distributedsystems.orders.reservation.services;

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

import com.distributedsystems.orders.reservation.components.ReservationConsumer;
import com.distributedsystems.orders.reservation.entities.Order;
import com.distributedsystems.orders.reservation.entities.OrderItem;
import com.distributedsystems.orders.reservation.entities.Product;
import com.distributedsystems.orders.reservation.repositories.OrdersRepository;
import com.distributedsystems.orders.reservation.repositories.ProductsRepository;

@Service("reservationService")
public class ReservationServiceImpl implements ReservationService {
  private static final Log LOG = LogFactory.getLog(ReservationConsumer.class);
	private static final String DEFAULT_EXCHANGE = "";
	
  @Autowired
  private RabbitTemplate rabbitTemplate;
  
  @Autowired
  private OrdersRepository ordersRepository;
  
  @Autowired
  private ProductsRepository productsRepository;
  
  @Autowired
  private Queue billingQueue;

  @Override
  public void sendOrderToBilling(int orderId) {
    Message body = new Message(String.valueOf(orderId).getBytes(), new MessageProperties());
    rabbitTemplate.send(DEFAULT_EXCHANGE, billingQueue.getName(), body);
  }

	@Override
	public void makeOrderReservation(int orderId) {
		Optional<Order> order = ordersRepository.findById(orderId);
		
		if(order.isPresent()) {
			LOG.info("[Reservation] Making reservation... ");
			LOG.info("[Reservation] Reservation complete, sending to billing... ");
			sendOrderToBilling(orderId);
		}
	}
}