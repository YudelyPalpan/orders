package com.distributedsystems.orders.billing.services;

public interface EmailService {

	void sendEmail(String to, String subject, String text);
}
