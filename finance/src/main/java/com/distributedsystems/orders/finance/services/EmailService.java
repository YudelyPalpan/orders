package com.distributedsystems.orders.finance.services;

import java.util.List;

import com.distributedsystems.orders.finance.util.MessageAttachment;

public interface EmailService {

	void sendEmail(String to, String subject, String text, List<MessageAttachment> attachments);
}
