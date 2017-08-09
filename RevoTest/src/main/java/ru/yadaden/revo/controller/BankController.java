package ru.yadaden.revo.controller;

import lombok.RequiredArgsConstructor;
import ru.yadaden.revo.service.BankService;
import spark.Request;
import spark.Response;

@RequiredArgsConstructor
public class BankController {

	private final BankService bankService;

	public String helloWorld(Request request, Response response) {
		return "Hello World";
	}
}