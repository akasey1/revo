package ru.yadaden.revo.service;

import java.util.List;

import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankException;

public interface AccountService {
	BankAccount createAccount(String userName);

	List<BankAccount> accountsByUser(String userName);

	BankAccount findAccount(String accountNumber) throws BankException;

	void transfer(String from, String to, long amount) throws BankException;

	void addMoney(String account, long amount) throws BankException;

}