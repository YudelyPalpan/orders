package com.distributedsystems.orders.billing.components;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.distributedsystems.orders.billing.entities.Customer;
import com.distributedsystems.orders.billing.entities.Order;
import com.distributedsystems.orders.billing.entities.OrderItem;
import com.distributedsystems.orders.billing.repositories.OrdersRepository;
import com.distributedsystems.orders.billing.services.EmailService;
import com.distributedsystems.orders.billing.services.WarehouseService;

@Component
public class WarehouseConsumer {
	
	private static final Log LOG = LogFactory.getLog(WarehouseConsumer.class);
	
	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private WarehouseService warehouseService;

	
	@Autowired
	private EmailService emailService;
	
	@RabbitListener(queues= { "warehouse" })
	public void checkWarehouseStock(@Payload byte[] payload) {
		int orderId = Integer.parseInt(new String(payload));
		
		LOG.info("Checking order " + orderId);
		
		warehouseService.checkOrderItemsStock(orderId);

	}

	/*
	private void sendOrderProcessingEmail(Order order) {
		
		Optional<String> email = Optional.of(order.getCustomer().getEmail());

		email.ifPresent(customerEmail -> {
			String subject = String.format("[Store] La orden de compra '%d' está siendo procesada", order.getId());

			String text = processingOrderMailBuilder
				.withTemplate("email/order_processing")
				.withOrder(order)
				.build();
			
			emailService.sendEmail(customerEmail, subject, text);
		});
	}
	*/
}
