package ru.yadaden.revo.service;

import lombok.RequiredArgsConstructor;
import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankException;
import ru.yadaden.revo.model.BankUser;
import ru.yadaden.revo.storage.BankStorage;

@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

	private final BankStorage storage;

	@Override
	public BankUser createUser(String userName) {
		BankUser user = new BankUser(userName);
		return storage.addUser(user);
	}

	@Override
	public BankAccount createAccount(String userName) {
		BankUser user = storage.userByName(userName);
		BankAccount account = new BankAccount(user);
		user.addAccount(account);
		return storage.addAccount(account);
	}

	@Override
	public void transfer(String from, String to, long amount) throws BankException {
		if (from.equals(to) || amount == 0) {
			// nothing to do
			return;
		}
		if (from.compareTo(to) > 0) {
			BankAccount fromAcc = findAccount(from);
			BankAccount toAcc = findAccount(to);
			synchronized (fromAcc) {
				synchronized (toAcc) {
					long fromAccNewAmount = calcAndValidateAmount(fromAcc.getAmount(), -amount);
					long toAccNewAmount = calcAndValidateAmount(toAcc.getAmount(), amount);
					fromAcc.setAmount(fromAccNewAmount);
					toAcc.setAmount(toAccNewAmount);
				}
			}
		} else {
			transfer(to, from, -amount);
		}
	}

	@Override
	public void addMoney(String account, long amount) throws BankException {
		if (account == null || amount == 0) {
			// nothing to do
			return;
		}
		BankAccount bankAccount = findAccount(account);
		synchronized (bankAccount) {
			long newAmount = calcAndValidateAmount(bankAccount.getAmount(), amount);
			bankAccount.setAmount(newAmount);
		}
	}

	private long calcAndValidateAmount(long oldAmount, long addition) throws BankException {
		long newAmount = oldAmount + addition;
		if (newAmount < 0) {
			throw new BankException("Not enough money to do transaction!");
		}
		return newAmount;
	}

	private BankAccount findAccount(String account) throws BankException {
		BankAccount bankAccount = storage.accountByNumber(account);
		if (bankAccount == null) {
			throw new BankException("Account " + account + " not found!");
		}
		return bankAccount;
	}
}