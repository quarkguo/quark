package com.ccg.common.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
	
	public void sendEmail(String subject, String content, String fromEmail, String[] toEmails){


        Properties props = new Properties();  
        props.put("mail.smtp.host", "pop-server.satx.rr.com");  
        //props.put("mail.smtp.auth", "true");  
        props.put("mail.debug", "true");  
        props.put("mail.smtp.port", 25);  
        props.put("mail.smtp.socketFactory.port", 25);  
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");

        Session mailSession = Session.getInstance(props);  

        try {

            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);

            message.setSubject(subject);
            message.setFrom(new InternetAddress(fromEmail));
            
            InternetAddress[] address = new InternetAddress[toEmails.length];
            for(int i = 0; i < address.length; i++){
            	address[i] = new InternetAddress(toEmails[i]);
            }
           
            message.addRecipients(Message.RecipientType.TO, address);
            
            String body = content;
            message.setContent(body,"text/html");
            transport.connect();

            transport.sendMessage(message,message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (Exception exception) {
        	exception.printStackTrace();
        }
    		
	}
}
