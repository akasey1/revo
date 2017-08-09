package ru.yadaden.revo.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankUser;

public class BankStorageImpl implements BankStorage {

	private final ConcurrentHashMap<String, BankAccount> accounts = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, BankUser> users = new ConcurrentHashMap<>();

	@Override
	public BankUser userByName(String username) {
		return users.get(username);
	}

	@Override
	public BankAccount accountByNumber(String accountNumber) {
		return accounts.get(accountNumber);
	}

	@Override
	public BankUser addUser(BankUser bankUser) {
		if (!users.containsKey(bankUser.getUserName())) {
			users.put(bankUser.getUserName(), bankUser);
		}
		return users.get(bankUser.getUserName());
	}

	@Override
	public BankAccount addAccount(BankAccount account) {
		if (!accounts.contains(account.getAccountNumber())) {
			accounts.put(account.getAccountNumber(), account);
		}
		return accounts.get(account.getAccountNumber());
	}

	@Override
	public List<BankUser> allUsers() {
		return Collections.unmodifiableList(new ArrayList<>(users.values()));
	}
}