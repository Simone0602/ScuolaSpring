package com.exprivia.demo.exception;

@SuppressWarnings("serial")
public class NotFoundTokenException extends RuntimeException{
	public NotFoundTokenException(String message) {
		super(message);
	}
}
