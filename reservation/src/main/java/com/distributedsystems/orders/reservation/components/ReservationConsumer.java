package com.distributedsystems.orders.reservation.components;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.distributedsystems.orders.reservation.repositories.OrdersRepository;
import com.distributedsystems.orders.reservation.services.EmailService;
import com.distributedsystems.orders.reservation.services.ReservationService;

@Component
public class ReservationConsumer {
	
	private static final Log LOG = LogFactory.getLog(ReservationConsumer.class);
	
	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private EmailService emailService;
	
	@RabbitListener(queues= { "reservation" })
	public void checkWarehouseStock(@Payload byte[] payload) {
		int orderId = Integer.parseInt(new String(payload));
		
		LOG.info("[Reservation] Checking order " + orderId);
		
		reservationService.makeOrderReservation(orderId);

	}
}
