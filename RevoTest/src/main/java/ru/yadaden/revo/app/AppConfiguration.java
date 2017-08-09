package ru.yadaden.revo.app;

import ru.yadaden.revo.controller.BankController;
import ru.yadaden.revo.service.AccountService;
import ru.yadaden.revo.service.UserService;
import ru.yadaden.revo.storage.BankStorage;

public interface AppConfiguration {

	BankController getControllerBean();

	UserService getUserServiceBean();

	AccountService getAccountServiceBean();

	BankStorage getStorageBean();

}