package com.distributedsystems.orders.processing.services;

public interface EmailService {

	void sendEmail(String to, String subject, String text);
}
