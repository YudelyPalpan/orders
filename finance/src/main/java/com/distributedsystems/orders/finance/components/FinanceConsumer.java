package com.distributedsystems.orders.finance.components;


import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.distributedsystems.orders.finance.services.FinanceService;
import com.distributedsystems.orders.finance.util.MessageAttachment;

@Component
public class FinanceConsumer {
	
	private static final Log LOG = LogFactory.getLog(FinanceConsumer.class);

	@Autowired
	private FinanceService financeService;
	
	@RabbitListener(queues= { "finance" })
	public void checkWarehouseStock(@Headers Map<String, Object> headers, @Payload byte[] fileBytes) {
		int orderId = (Integer) headers.get("orderId");
		String contentType =  (String) headers.get("mimeType");
		LOG.info("[Finances] Checking order " + orderId);
		MessageAttachment attachment = new MessageAttachment("Order_" + orderId + ".pdf", fileBytes, contentType);
		financeService.makeFinanceProcessing(orderId, attachment);

	}
}
