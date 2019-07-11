package com.distributedsystems.orders.billing.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.distributedsystems.orders.billing.repositories.OrdersRepository;
import com.distributedsystems.orders.billing.services.EmailService;
import com.distributedsystems.orders.billing.services.BillingService;

@Component
public class BillingConsumer {
	
	private static final Log LOG = LogFactory.getLog(BillingConsumer.class);
	
	@Autowired
	private BillingService billingService;

	@Autowired
	private EmailService emailService;
	
	@RabbitListener(queues= { "billing" })
	public void orderBilling(@Payload byte[] payload) {
		int orderId = Integer.parseInt(new String(payload));
		
		LOG.info("[Billing] Billing order '" + orderId + "'... ");
		
		billingService.billOrder(orderId);

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
