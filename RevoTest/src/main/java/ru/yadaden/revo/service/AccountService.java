package ru.yadaden.revo.service;

import java.util.List;

import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankException;

/**
 * Service for operations with accounts. Throws {@link BankException} on wrong
 * params
 */
public interface AccountService {
	BankAccount createAccount(String userName);

	List<BankAccount> accountsByUser(String userName);

	/**
	 * @param from
	 *            account number to get money from
	 * @param to
	 *            account number to transfer money to
	 * @param amount
	 * @throws BankException
	 *             if not enough money or any account wasn't found
	 */
	void transfer(String from, String to, long amount) throws BankException;

	/**
	 * @param account
	 * @param amount
	 *            may be negative (cash disbursment)
	 * @throws BankException
	 *             if not enough money on account or account not found
	 */
	void addMoney(String account, long amount) throws BankException;

	BankAccount getAccount(String account);

}