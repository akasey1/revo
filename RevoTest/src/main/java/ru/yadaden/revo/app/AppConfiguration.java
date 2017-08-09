package ru.yadaden.revo.app;

import ru.yadaden.revo.controller.BankController;
import ru.yadaden.revo.service.BankService;
import ru.yadaden.revo.storage.BankStorage;

public interface AppConfiguration {

	BankController getControllerBean();

	BankService getServiceBean();

	BankStorage getStorageBean();

}