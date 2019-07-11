package com.distributedsystems.orders.reservation.services;

public interface EmailService {

	void sendEmail(String to, String subject, String text);
}
