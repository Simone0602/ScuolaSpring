package com.exprivia.demo.exception;

@SuppressWarnings("serial")
public class NotFoundDocenteException extends RuntimeException{
	public NotFoundDocenteException(String message) {
		super(message);
	}
}
