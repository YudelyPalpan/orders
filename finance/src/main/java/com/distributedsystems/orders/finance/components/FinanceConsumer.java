package com.distributedsystems.orders.finance.components;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.distributedsystems.orders.finance.services.EmailService;
import com.distributedsystems.orders.finance.services.FinanceService;

@Component
public class FinanceConsumer {
	
	private static final Log LOG = LogFactory.getLog(FinanceConsumer.class);

	@Autowired
	private FinanceService financeService;

	@Autowired
	private EmailService emailService;
	
	@RabbitListener(queues= { "finance" })
	public void checkWarehouseStock(@Payload byte[] payload) {
		int orderId = Integer.parseInt(new String(payload));
		
		LOG.info("[Finance] Checking order " + orderId);
		
		financeService.makeFinanceProcessing(orderId);

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
