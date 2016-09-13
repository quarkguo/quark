package com.ccg.common.util;

import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ccg.common.data.user.NewUserEmailConfig;
import com.ccg.util.ConfigurationManager;

public class SendEmail {
	
	public static void sendEmail(String subject, String content, String fromEmail, String[] toEmails){


        Properties props = new Properties();  
        props.put("mail.smtp.host", "pop-server.satx.rr.com");  
        //props.put("mail.smtp.auth", "true");  
        props.put("mail.debug", "false");  
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
	
	public static void sendCreateNewUserEmail(String emailAddress, String password){
		/*
		 * Email template, please put this template at location of
		 * 		apache-tomcat-8.0.33/ccgconfig/com.ccg.common.data.user.NewUserEmailConfig.xml
		 
			<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
			<newUserEmailConfig>
			    <subject>Your New Account Information</subject>
			    <fromEmail>admin@ccg.com</fromEmail>
			    <content>
			A new account has been create for you.
			Account ID: {0}
			Password: {1}
			Please login and change your passwod.
			    </content>
			</newUserEmailConfig>		 
		*/
		String[] toEmails = {emailAddress};
		
		NewUserEmailConfig config = ConfigurationManager.getConfig(NewUserEmailConfig.class);
		String content = config.getContent();
		String subject = config.getSubject();
		String fromEmail = config.getFromEmail();

		MessageFormat mf = new MessageFormat(content);
		Object[] objArray = {emailAddress, password};
		content = mf.format(objArray);
		content = content.replace("\n", "<br />");		
		sendEmail(subject, content, fromEmail, toEmails);		
	}	
}
