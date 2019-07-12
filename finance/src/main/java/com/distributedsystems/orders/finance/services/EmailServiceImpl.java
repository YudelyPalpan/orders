package com.distributedsystems.orders.finance.services;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.distributedsystems.orders.finance.util.MessageAttachment;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

    private static final Log LOG = LogFactory.getLog(EmailServiceImpl.class);
    
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendEmail(String to, String subject, String text, List<MessageAttachment> attachments) {
        
    	try {
        	LOG.info("Sending email to '" + to + "' subject [" + subject + "]");
        	
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
                messageHelper.setTo(to);
                messageHelper.setSubject(subject);
                messageHelper.setText(text, true);
                for(MessageAttachment attachment : attachments) {
                	messageHelper.addAttachment(attachment.getFileName(), attachment.getFileInputStreamResource());
                }
            };

            emailSender.send(messagePreparator);
        } catch (MailException ex) {
            throw ex;
        }
    }
}