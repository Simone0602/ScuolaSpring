package com.exprivia.demo.exception;

@SuppressWarnings("serial")
public class IllegalPasswordException extends RuntimeException{
	public IllegalPasswordException(String message) {
		super(message);
	}
}
