package com.logesh.libmgmt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserBook {

	private String userName;
	private String bookName;
	private String userEmailId;
	private String returnDate;

	private String daysLeftToReturn;

	public UserBook(String userName, String userEmailId, String returnDate) {
		super();
		this.userName = userName;
		this.userEmailId = userEmailId;
		this.returnDate = returnDate;
	}

	public UserBook(String userName, String bookName, String userEmailId, String returnDate) {
		super();
		this.userName = userName;
		this.bookName = bookName;
		this.userEmailId = userEmailId;
		this.returnDate = returnDate;
	}

}
