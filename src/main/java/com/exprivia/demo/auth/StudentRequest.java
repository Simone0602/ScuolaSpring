package com.exprivia.demo.auth;

public class StudentRequest {
	private String userCode;
	public String password;
	
	public StudentRequest(String userCode, String password) {
		this.userCode = userCode;
		this.password = password;
	}
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
