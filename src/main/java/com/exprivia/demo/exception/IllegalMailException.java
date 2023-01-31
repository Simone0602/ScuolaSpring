package com.exprivia.demo.exception;

@SuppressWarnings("serial")
public class IllegalMailException extends RuntimeException {
	public IllegalMailException(String message) {
		super(message);
	}
}
