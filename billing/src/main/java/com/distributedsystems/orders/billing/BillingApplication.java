package com.distributedsystems.orders.billing;

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
public class BillingApplication {

	@Value("${queue.billing.name}")
  private String billingQueue;
	
	@Value("${queue.finance.name}")
  private String financeQueue;

	public static void main(String[] args) {
		SpringApplication.run(BillingApplication.class, args);
	}
	
	@Bean
	public Queue billingQueue() {
		return new Queue(billingQueue, true);
	}

	@Bean
	public Queue financeQueue() {
		return new Queue(financeQueue, true);
	}
}
