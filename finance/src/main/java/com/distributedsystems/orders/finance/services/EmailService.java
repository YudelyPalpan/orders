package com.distributedsystems.orders.finance.services;

public interface EmailService {

	void sendEmail(String to, String subject, String text);
}
