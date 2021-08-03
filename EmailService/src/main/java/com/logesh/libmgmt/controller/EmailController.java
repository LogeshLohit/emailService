package com.logesh.libmgmt.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logesh.libmgmt.service.EmailService;

@RestController
public class EmailController {

	@Autowired
	public EmailService emailService;

	@GetMapping("greet")
	public String greetUser() {
		try {
			emailService.sendmail();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Hello, user!";
	}
}
