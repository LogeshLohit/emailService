package com.logesh.libmgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logesh.libmgmt.service.EmailService;
import com.logesh.libmgmt.service.AlertBookRtDtEmailService;

/**
 * @author Logesh
 *
 */
@RestController
public class AlertBookRtDtEmailController {

	@Autowired
	public EmailService emailService;

	@Autowired
	public AlertBookRtDtEmailService userService;

	@GetMapping("greet")
	public String greetUser() {
		return "Hello, user!";
	}

	@GetMapping("triggerAlertEmail")
	public String triggerAlertEmail(@RequestParam(name = "alertDays", required = false) String noOfDays) {
		System.out.println("Alert Days found: " + noOfDays);
		userService.validateUserDate(noOfDays);
		return "Alert email process completed!";
	}
}
