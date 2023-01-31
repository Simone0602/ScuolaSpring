package com.exprivia.demo.exception;

@SuppressWarnings("serial")
public class DontSendEmailException extends RuntimeException {
	
	public DontSendEmailException(String message) {
		super(message);
	}
	
}
