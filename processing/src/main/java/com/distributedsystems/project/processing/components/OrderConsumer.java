package com.distributedsystems.project.processing.components;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.distributedsystems.project.processing.services.WarehouseService;
import com.distributedsystems.project.processing.entities.Customer;
import com.distributedsystems.project.processing.entities.Order;
import com.distributedsystems.project.processing.entities.OrderItem;
import com.distributedsystems.project.processing.repositories.OrdersRepository;
import com.distributedsystems.project.processing.services.EmailService;

@Component
public class OrderConsumer {
	
	private static final Log LOG = LogFactory.getLog(OrderConsumer.class);
	
	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private ProcessingOrderMailBuilder processingOrderMailBuilder;
	
	@Autowired
	private EmailService emailService;
	
	@RabbitListener(queues= { "orders" })
	public void processOrders(@Payload byte[] payload) {
		
		int orderId = Integer.parseInt(new String(payload));
		
		LOG.info("Processing order with id '" + orderId + "'");
		
		Optional<Order> order = ordersRepository.findById(orderId);
		
		order.ifPresent(findOrder -> {
			// Printing log
			printOrderLog(findOrder);
			
			// Sending email
			//sendOrderProcessingEmail(findOrder);

			// Send to warehouse to stock check
			warehouseService.sendOrderToStockChecking(findOrder.getId());
		});
	}

	private void printOrderLog(Order order) {
		
		Customer customer = order.getCustomer();
		List<OrderItem> orderItems = order.getItems();

		LOG.info("## Order founded! ##");
		LOG.info(String.format("* OrderDate = %s", order.getOrderDate()));
		LOG.info(String.format("* OrderNumber = %s", order.getOrderNumber()));
		LOG.info(String.format("* Customer = %s %s", customer.getFirstName(), customer.getLastName()));
		
		LOG.info("* Items = ");
		
		LOG.info(String.format("| Product Name | Unit Price | Quantity |"));
		
		for(OrderItem orderItem : orderItems) {
			
			String productName = orderItem.getProduct().getProductName();
			double unitPrice =  orderItem.getUnitPrice();
			int quantity = orderItem.getQuantity();
			
			LOG.info(String.format("| %s | $%.2f | %d |", productName, unitPrice, quantity));
		}
		
		LOG.info(String.format("* Total amount: $ %.2f", order.getTotalAmount()));
	}
	
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
}
