package com.distributedsystems.orders.finance.services;

import java.util.Collections;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distributedsystems.orders.finance.components.FinanceConsumer;
import com.distributedsystems.orders.finance.components.InvoiceEmailBuilder;
import com.distributedsystems.orders.finance.entities.Order;
import com.distributedsystems.orders.finance.repositories.OrdersRepository;
import com.distributedsystems.orders.finance.util.MessageAttachment;

@Service("financeService")
public class FinanceServiceImpl implements FinanceService {
	private static final Log LOG = LogFactory.getLog(FinanceConsumer.class);

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private InvoiceEmailBuilder invoiceEmailBuilder;

	@Override
	public void makeFinanceProcessing(int orderId, MessageAttachment orderFile) {
		Optional<Order> order = ordersRepository.findById(orderId);

		if (order.isPresent()) {
			LOG.info("[Finances] Starting finance process for order'" + orderId + "'... ");
			LOG.info("[Finances] Execute accounting cheking for order'" + orderId + "'... ");
			LOG.info("[Finances] Declaring bill for order '" + orderId + "' ... ");
			LOG.info("[Finances] Sending invoice for order '" + orderId + "'... ");
			LOG.info("[Finances] Finance process for order'" + orderId + "'complete ");
			sendOrderProcessingEmail(order.get(), orderFile);
		}
	}

	private void sendOrderProcessingEmail(Order order, MessageAttachment orderFile) {

		Optional<String> email = Optional.of(order.getCustomer().getEmail());

		email.ifPresent(customerEmail -> {
			String subject = String.format("[Store] Factura de la orden de compra '%d'", order.getId());

			String text = invoiceEmailBuilder
					.withTemplate("email/invoice")
					.withOrder(order)
					.build();

			emailService.sendEmail(customerEmail, subject, text, Collections.singletonList(orderFile));
		});
	}

}