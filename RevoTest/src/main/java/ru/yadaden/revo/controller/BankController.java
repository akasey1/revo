package ru.yadaden.revo.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankException;
import ru.yadaden.revo.model.BankUser;
import ru.yadaden.revo.service.AccountService;
import ru.yadaden.revo.service.UserService;
import spark.Request;
import spark.Response;

/**
 * Controller is used to pass user requests to service beans, return response,
 * validate parameters
 */
@RequiredArgsConstructor
public class BankController {

	private final UserService userService;
	private final AccountService accountService;

	public List<BankUser> allUsers(Request request, Response response) {
		return userService.getAllUsers();
	}

	public BankUser addUser(Request request, Response response) throws BankException {
		ParsedRequest parsed = new ParsedRequest(request);
		validateUsername(parsed);
		return userService.createUser(parsed.getUsername());
	}

	public BankUser getUser(Request request, Response response) throws BankException {
		ParsedRequest parsed = new ParsedRequest(request);
		validateUsername(parsed);
		return userService.findUser(parsed.getUsername());
	}

	public BankAccount addAccount(Request request, Response response) throws BankException {
		ParsedRequest parsed = new ParsedRequest(request);
		validateUsername(parsed);
		return accountService.createAccount(parsed.getUsername());
	}

	public BankAccount getAccount(Request request, Response response) throws BankException {
		ParsedRequest parsed = new ParsedRequest(request);
		return accountService.getAccount(parsed.getAccount());
	}

	public BankAccount addMoney(Request request, Response response) throws BankException {
		ParsedRequest parsed = new ParsedRequest(request);
		long amount = parseAmount(parsed);
		accountService.addMoney(parsed.getAccount(), amount);
		return accountService.getAccount(parsed.getAccount());
	}

	public String transfer(Request request, Response response) throws BankException {
		ParsedRequest parsed = new ParsedRequest(request);
		long amount = parseAmount(parsed);
		accountService.transfer(parsed.getFrom(), parsed.getTo(), amount);
		return "OK";
	}

	private long parseAmount(ParsedRequest parsed) throws BankException {
		long amount;
		try {
			amount = Long.parseLong(parsed.getAmount());
		} catch (NumberFormatException e) {
			throw new BankException("Amount is not a number (wrong format).");
		}
		return amount;
	}

	private void validateUsername(ParsedRequest parsed) throws BankException {
		if (parsed.getUsername() == null || "".equals(parsed.getUsername())) {
			throw new BankException("Username is not defined!");
		}
	}
}