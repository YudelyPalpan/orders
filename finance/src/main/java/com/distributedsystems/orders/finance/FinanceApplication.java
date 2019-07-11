package com.distributedsystems.orders.finance;

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
public class FinanceApplication {

  @Value("${queue.finance.name}")
  private String financeQueue;
	
  public static void main(String[] args) {
    SpringApplication.run(FinanceApplication.class, args);
  }
	
  @Bean
  public Queue financeQueue() {
    return new Queue(financeQueue, true);
  }
}
