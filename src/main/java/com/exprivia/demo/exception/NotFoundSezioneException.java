package com.exprivia.demo.exception;

@SuppressWarnings("serial")
public class NotFoundSezioneException extends RuntimeException{
	public NotFoundSezioneException(String message) {
		super(message);
	}
}
