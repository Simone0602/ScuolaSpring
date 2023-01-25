package com.exprivia.demo.exception;

@SuppressWarnings("serial")
public class NotFoundStudentException extends RuntimeException{
	
	public NotFoundStudentException(String message) {
		super(message);
	}
}
