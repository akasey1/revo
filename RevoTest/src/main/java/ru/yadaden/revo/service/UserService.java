package ru.yadaden.revo.service;

import java.util.List;

import ru.yadaden.revo.model.BankException;
import ru.yadaden.revo.model.BankUser;

/**
 * Service for operations with users. Throws {@link BankException} on wrong
 * params
 */
public interface UserService {

	List<BankUser> getAllUsers();

	BankUser findUser(String userName);

	BankUser createUser(String userName);
}