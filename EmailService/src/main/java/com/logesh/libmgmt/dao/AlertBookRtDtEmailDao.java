package com.logesh.libmgmt.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.logesh.libmgmt.model.BookByUser;

/**
 * @author Logesh
 *
 */
@Service
public class AlertBookRtDtEmailDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${alertEmail.mysql.query}")
	public String selectUserQuery;

	public List<BookByUser> getAllBookUsers() {
		System.out.println("Trying to connect to DB ...");

		List<Map<String, Object>> usersList = jdbcTemplate.queryForList(selectUserQuery);

		System.out.println("DB results..: " + usersList);

		List<BookByUser> bookRtDtByUserList = new ArrayList<BookByUser>();

		usersList.stream().forEach(userFromDb -> {

			bookRtDtByUserList.add(buildUserByBookModel(userFromDb));
		});

		return bookRtDtByUserList;
	}

	private BookByUser buildUserByBookModel(Map<String, Object> userFromDb) {
		BookByUser bookByUser = new BookByUser();

		bookByUser.setUserName(Objects.toString(userFromDb.get("user_name"), StringUtils.EMPTY));
		bookByUser.setBookName(Objects.toString(userFromDb.get("book_name"), StringUtils.EMPTY));
		bookByUser.setUserEmailId(Objects.toString(userFromDb.get("email_id"), StringUtils.EMPTY));
		bookByUser.setReturnDate(Objects.toString(userFromDb.get("return_date"), StringUtils.EMPTY));

		return bookByUser;
	}
}
