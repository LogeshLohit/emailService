package com.logesh.libmgmt.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.logesh.libmgmt.model.UserBook;

@Service
public class UserService {

	@Value("${email.noOfDaysToValidate:3}")
	public long noOfBeforeToTriggerEmail;

	public final String RETURN_DATE_FORMAT = "yyyy-MM-dd";

	@Autowired
	private EmailService emailService;

	public void validateUserDate(String noOfDays) {

		if (StringUtils.isNotBlank(noOfDays) && NumberUtils.isCreatable(noOfDays)) {
			noOfBeforeToTriggerEmail = NumberUtils.toLong(noOfDays);

			System.out.println("No. of days to validate and trigger email is overrided to :" + noOfDays);
		}

		// validateReturnDate
		List<UserBook> userList = validateBookReturnDate();

		// if returnDate less that specified Date eg. 3
		triggerEmailAlert(userList);

	}

	private void triggerEmailAlert(List<UserBook> userList) {

		userList.stream().forEach(userInfo -> {
			try {
				emailService.sendmail(userInfo);
			} catch (MessagingException | IOException e) {
				System.out.println("Exception occured while triggering email: " + e.getMessage());
				e.printStackTrace();
			}
		});
	}

	private List<UserBook> validateBookReturnDate() {
		// TODO: QUERY DB to get the userData
		List<UserBook> userData = getTempUserData();

		return getAllUserByAlertDays(userData);

	}

	private List<UserBook> getAllUserByAlertDays(List<UserBook> userData) {

		return userData.stream().filter(userInfo -> validateEachUserData(userInfo)).collect(Collectors.toList());

	}

	private boolean validateEachUserData(UserBook userInfo) {

		LocalDate returnDate = parseReturnDate(userInfo.getReturnDate());

		if (!ObjectUtils.isEmpty(returnDate)) {

			long daysDiff = getDiffBtwCurrDateAndRetDate(returnDate);

			userInfo.setDaysLeftToReturn(String.valueOf(daysDiff));

			return validateReturnDays(daysDiff);
		} else {
			System.out.println("Empty or NULL returnDate : for the user: " + userInfo);
			return false;
		}

	}

	private boolean validateReturnDays(long daysDiff) {
		return daysDiff <= noOfBeforeToTriggerEmail;
	}

	private long getDiffBtwCurrDateAndRetDate(LocalDate returnDate) {
		LocalDate currentDate = LocalDate.now();

		Period dateDifference = Period.between(currentDate, returnDate);

		long daysDiff = dateDifference.getDays();

		System.out.println("Book's returnDate: " + returnDate + ", currentDate: " + currentDate
				+ ", and the difference between these two days is : " + daysDiff);

		return daysDiff;

	}

	private LocalDate parseReturnDate(String returnDate) {

		if (StringUtils.isNotBlank(returnDate) && !StringUtils.containsIgnoreCase(returnDate, "NULL")) {
			DateTimeFormatter bookReturnDateFormatter = DateTimeFormatter.ofPattern(RETURN_DATE_FORMAT);

			LocalDate parsedReturnDate = LocalDate.parse(returnDate, bookReturnDateFormatter);

			return parsedReturnDate;

		}

		return null;
	}

	private List<UserBook> getTempUserData() {

		UserBook user1 = new UserBook("Mukesh", "The Alchemist", "ABC@gmail.com", "2021-08-06");
		UserBook user2 = new UserBook("Logesh", "Insurrection", "EFG@gmail.com", "2021-08-09");

		List<UserBook> userList = Arrays.asList(user1, user2);

		return userList;

	}
}
