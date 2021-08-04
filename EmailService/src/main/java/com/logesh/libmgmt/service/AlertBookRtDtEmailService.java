package com.logesh.libmgmt.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import com.logesh.libmgmt.dao.AlertBookRtDtEmailDao;
import com.logesh.libmgmt.model.BookByUser;

/**
 * @author Logesh
 *
 */
@Service
public class AlertBookRtDtEmailService {

	@Value("${email.noOfDaysToValidate:3}")
	public long noOfBeforeToTriggerEmail;

	@Value("${alertEmail.testing.returnDate1}")
	public String testReturnDate1;

	@Value("${alertEmail.testing.returnDate1}")
	public String testReturnDate2;

	@Value("${alertEmail.testing.emailId1}")
	public String testEmailId1;

	@Value("${alertEmail.testing.emailId2}")
	public String testEmailId2;

	@Value("${alertEmail.returnDate.timeStampFormat}")
	public String returnDateFormat;

	@Value("${alertEmail.getFromDb}")
	public boolean getFromDbSwitch;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AlertBookRtDtEmailDao alertEmailDao;

	public void validateUserDate(String noOfDays) {

		if (StringUtils.isNotBlank(noOfDays) && NumberUtils.isCreatable(noOfDays)) {
			noOfBeforeToTriggerEmail = NumberUtils.toLong(noOfDays);

			System.out.println("No. of days to validate and trigger email is overrided to :" + noOfDays);
		}

		// validateReturnDate
		List<BookByUser> userList = validateBookReturnDate();

		// if returnDate less that specified Date eg. 3
		triggerEmailAlert(userList);

	}

	private void triggerEmailAlert(List<BookByUser> userList) {

		userList.stream().forEach(userInfo -> {
			try {
				emailService.sendmail(userInfo);
			} catch (MessagingException | IOException e) {
				System.out.println("Exception occured while triggering email: " + e.getMessage());
				e.printStackTrace();
			}
		});
	}

	private List<BookByUser> validateBookReturnDate() {
		List<BookByUser> userData = new ArrayList<BookByUser>();

		if (getFromDbSwitch) {
			userData = alertEmailDao.getAllBookUsers();
		} else {
			userData = getTempUserData();
		}
		
		return getAllUserByAlertDays(userData);

	}

	private List<BookByUser> getAllUserByAlertDays(List<BookByUser> userData) {

		return userData.stream().filter(userInfo -> validateEachUserData(userInfo)).collect(Collectors.toList());

	}

	private boolean validateEachUserData(BookByUser userInfo) {

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
			DateTimeFormatter bookReturnDateFormatter = DateTimeFormatter.ofPattern(returnDateFormat);

			LocalDate parsedReturnDate = LocalDate.parse(returnDate, bookReturnDateFormatter);

			return parsedReturnDate;

		}

		return null;
	}

	private List<BookByUser> getTempUserData() {

		BookByUser user1 = new BookByUser("Mukesh", "The Alchemist", testEmailId1, testReturnDate1);
		BookByUser user2 = new BookByUser("Logesh", "Insurrection", testEmailId2, testReturnDate2);

		List<BookByUser> userList = Arrays.asList(user1, user2);

		return userList;

	}
}
