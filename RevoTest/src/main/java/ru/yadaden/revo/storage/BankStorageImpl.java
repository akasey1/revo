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
		return users.computeIfAbsent(bankUser.getUserName(), (k) -> bankUser);
	}

	@Override
	public BankAccount addAccount(BankAccount account) {
		return accounts.computeIfAbsent(account.getAccountNumber(), (k) -> account);
	}

	@Override
	public List<BankUser> allUsers() {
		return Collections.unmodifiableList(new ArrayList<>(users.values()));
	}
}