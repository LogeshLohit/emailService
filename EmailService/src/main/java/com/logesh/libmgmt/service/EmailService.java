package com.logesh.libmgmt.service;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	public void sendmail() throws AddressException, MessagingException, IOException {
		System.out.println("Started method....");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("logesh.jeppiaar@gmail.com", "<PASSWORD!>");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("logesh.jeppiaar@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("logesh.ssg@gmail.com"));
		msg.setSubject("Email through Java APP");
		msg.setContent("Hey, guys!!", "text/html");
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent("MSG content", "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		MimeBodyPart attachPart = new MimeBodyPart();

		attachPart.attachFile("\\Pictures\\Screenshots\\s.png");
		multipart.addBodyPart(attachPart);
		msg.setContent(multipart);
		Transport.send(msg);

		System.out.println("Method ends....");
	}

}
