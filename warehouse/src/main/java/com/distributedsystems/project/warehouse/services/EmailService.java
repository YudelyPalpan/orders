package com.distributedsystems.project.warehouse.services;

public interface EmailService {

	void sendEmail(String to, String subject, String text);
}
