package com.distributedsystems.orders.reservation;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.amqp.core.Queue;

@SpringBootApplication
@EnableRabbit
@EnableScheduling
public class ReservationApplication {

	@Value("${queue.reservation.name}")
	private String reservationQueue;
	
	
	@Value("${queue.billing.name}")
  private String billingQueue;

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
	}
	
	@Bean
	public Queue reservationQueue() {
		return new Queue(reservationQueue, true);
	}

	@Bean
	public Queue billingQueue() {
		return new Queue(billingQueue, true);
	}
}
