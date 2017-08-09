package ru.yadaden.revo.service;

import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import ru.yadaden.revo.model.BankUser;
import ru.yadaden.revo.storage.BankStorage;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final BankStorage storage;

	@Override
	public BankUser createUser(String userName) {
		BankUser user = new BankUser(userName);
		return storage.addUser(user);
	}

	@Override
	public List<BankUser> getAllUsers() {
		return Collections.unmodifiableList(storage.allUsers());
	}

	@Override
	public BankUser findUser(String userName) {
		return storage.userByName(userName);
	}
}