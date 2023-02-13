package com.exprivia.demo.exception;

@SuppressWarnings("serial")
public class NotFoundClasseException extends RuntimeException {
	public NotFoundClasseException(String message) {
		super(message);
	}
}
