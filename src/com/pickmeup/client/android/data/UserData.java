package com.pickmeup.client.android.data;

public class UserData {
	private final String userName;
	private final String phoneNumber;
	
	public UserData(String userName, String phoneNumber) {
		this.userName = userName;
		this.phoneNumber = phoneNumber;
	}
	
	public String getUserName() {
		return userName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
}
