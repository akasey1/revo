package ru.yadaden.revo.controller;

import java.util.List;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankException;
import ru.yadaden.revo.model.BankUser;
import ru.yadaden.revo.service.AccountService;
import ru.yadaden.revo.service.UserService;
import spark.Request;
import spark.Response;

@RequiredArgsConstructor
public class BankController {

	public static final String TO = "to";
	public static final String FROM = "from";
	public static final String AMOUNT = "amount";
	public static final String ACCOUNT = "account";
	public static final String USERNAME = "username";

	private final UserService userService;
	private final AccountService accountService;

	public List<BankUser> allUsers(Request request, Response response) {
		return userService.getAllUsers();
	}

	public BankUser addUser(Request request, Response response) {
		return userService.createUser(request.queryParams(USERNAME));
	}

	public BankUser getUser(Request request, Response response) {
		return userService.findUser(request.queryParams(USERNAME));
	}

	public BankAccount addAccount(Request request, Response response) {
		return accountService.createAccount(request.queryParams(USERNAME));
	}

	public BankAccount getAccount(Request request, Response response) throws BankException {
		return accountService.findAccount(request.queryParams(ACCOUNT));
	}

	public BankAccount addMoney(Request request, Response response) throws BankException {
		accountService.addMoney(request.queryParams(ACCOUNT), Long.parseLong(request.queryParams(AMOUNT)));
		return accountService.findAccount(request.queryParams(ACCOUNT));
	}

	public String transfer(Request request, Response response) throws BankException {
		accountService.transfer(request.queryParams(FROM), request.queryParams(TO),
				Long.parseLong(request.queryParams(AMOUNT)));
		return "OK";
	}

	public String toJson(Object object) {
		return new Gson().toJson(object);
	}
}