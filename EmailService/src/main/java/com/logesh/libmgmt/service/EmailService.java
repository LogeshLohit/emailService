package com.logesh.libmgmt.service;

import java.io.IOException;
import java.text.MessageFormat;
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.logesh.libmgmt.model.BookByUser;

/**
 * @author Logesh
 *
 */
@Service
public class EmailService {

	@Value("${email.user.fromAddress.emailId}")
	public String fromEmailId;

	@Value("${email.user.fromAddress.emailPassword}")
	public String fromEmailPassword;

	@Value("${email.subject}")
	public String emailSubject;

	@Value("${email.body}")
	public String emailContent;

	public void sendmail(BookByUser userData) throws AddressException, MessagingException, IOException {
		System.out.println("Started method....");

		String emailBody = MessageFormat.format(emailContent, StringUtils.capitalize(userData.getUserName()),
				userData.getBookName(), userData.getReturnDate(), userData.getDaysLeftToReturn());

		System.out.println("Email content: " + emailBody);

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmailId, fromEmailPassword);
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(fromEmailId, false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userData.getUserEmailId()));
		msg.setSubject(emailSubject);
		msg.setContent(emailBody, "text/html");
		msg.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailBody, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
//		MimeBodyPart attachPart = new MimeBodyPart();

//		attachPart.attachFile("\\Pictures\\Screenshots\\s.png");
//		multipart.addBodyPart(attachPart);
		msg.setContent(multipart);
		Transport.send(msg);

		System.out.println("Email successfully sent for user:" + userData.getUserName());
	}

}
