package ru.yadaden.revo.storage;

import java.util.List;

import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankUser;

public interface BankStorage {

	BankUser userByName(String username);

	List<BankUser> allUsers();

	BankAccount accountByNumber(String accountNumber);

	/**
	 * @param bankUser
	 *            user to add
	 * @return added user or user with this username if already found
	 */
	BankUser addUser(BankUser bankUser);

	BankAccount addAccount(BankAccount account);

}