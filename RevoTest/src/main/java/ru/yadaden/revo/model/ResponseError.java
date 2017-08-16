package ru.yadaden.revo.model;

import lombok.Getter;

@Getter
public class ResponseError {
	private final String message;
	// marker to check in json for error
	private final boolean error;

	public ResponseError(String message, String... args) {
		this.message = String.format(message, args);
		this.error = true;
	}

	public ResponseError(Exception e) {
		this.message = e.getMessage();
		this.error = true;
	}
}