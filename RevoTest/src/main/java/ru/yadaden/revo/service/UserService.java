package ru.yadaden.revo.service;

import java.util.List;

import ru.yadaden.revo.model.BankUser;

public interface UserService {

	List<BankUser> getAllUsers();

	BankUser findUser(String userName);

	BankUser createUser(String userName);
}