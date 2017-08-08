package ru.yadaden.revo.service;

import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankException;
import ru.yadaden.revo.model.BankUser;

public interface BankService {

	BankUser createUser(String userName);

	BankAccount createAccount(String userName);

	public void transfer(String from, String to, long amount) throws BankException;

	/**
	 * @param account
	 *            to be modifed
	 * @param amount
	 *            may be negative or zero
	 * @throws BankException
	 */
	public void addMoney(String account, long amount) throws BankException;

}
