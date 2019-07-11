package com.distributedsystems.project.processing.services;

public interface EmailService {

	void sendEmail(String to, String subject, String text);
}
