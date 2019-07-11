package com.distributedsystems.orders.processing;

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
public class ProcessingApplication {

	@Value("${queue.orders.name}")
  private String ordersQueue;
	
	@Value("${queue.warehouse.name}")
  private String warehouseQueue;

	public static void main(String[] args) {
		SpringApplication.run(ProcessingApplication.class, args);
	}
	
	@Bean
	public Queue ordersQueue() {
		return new Queue(ordersQueue, true);
	}

	@Bean
	public Queue warehouseQueue() {
		return new Queue(warehouseQueue, true);
	}
}
