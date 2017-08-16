package ru.yadaden.revo.controller;

import lombok.Getter;
import spark.Request;

@Getter
public class ParsedRequest {
	private final String to;
	private final String from;
	private final String amount;
	private final String account;
	private final String username;

	public ParsedRequest(Request req) {
		to = req.queryParams("to");
		from = req.queryParams("from");
		amount = req.queryParams("amount");
		account = req.queryParams("account");
		username = req.queryParams("username");
	}
}