package ru.yadaden.revo.app;

import static spark.Spark.get;

import ru.yadaden.revo.controller.BankController;
import ru.yadaden.revo.service.BankService;
import ru.yadaden.revo.service.BankServiceImpl;
import ru.yadaden.revo.storage.BankStorage;
import ru.yadaden.revo.storage.BankStorageImpl;

public class TransfersApp implements AppConfiguration {

	public static void main(String[] args) {
		new TransfersApp();
	}

	private TransfersApp() {
		BankController controller = getControllerBean();
		get("/hello", controller::helloWorld);
	}

	@Override
	public BankController getControllerBean() {
		return new BankController(getServiceBean());
	}

	@Override
	public BankService getServiceBean() {
		return new BankServiceImpl(getStorageBean());
	}

	@Override
	public BankStorage getStorageBean() {
		return new BankStorageImpl();
	}
}