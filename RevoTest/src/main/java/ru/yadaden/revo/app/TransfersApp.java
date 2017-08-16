package ru.yadaden.revo.app;

import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.stop;

import com.google.gson.Gson;

import ru.yadaden.revo.controller.BankController;
import ru.yadaden.revo.model.BankException;
import ru.yadaden.revo.model.ResponseError;
import ru.yadaden.revo.service.AccountService;
import ru.yadaden.revo.service.AccountServiceImpl;
import ru.yadaden.revo.service.UserService;
import ru.yadaden.revo.service.UserServiceImpl;
import ru.yadaden.revo.storage.BankStorage;
import ru.yadaden.revo.storage.BankStorageImpl;

public class TransfersApp implements AppConfiguration {

	private BankStorage bankStorage;
	private BankController controller;

	public static void main(String[] args) {
		new TransfersApp();
	}

	private TransfersApp() {
		controller = getControllerBean();

		get("/users", controller::allUsers, this::toJson);
		post("/users", controller::addUser, this::toJson);

		get("/user", controller::getUser, this::toJson);

		get("/account", controller::getAccount, this::toJson);
		post("/account", controller::addAccount, this::toJson);
		post("/account/add", controller::addMoney, this::toJson);
		post("/account/transfer", controller::transfer, this::toJson);

		after((req, res) -> {
			res.type("application/json");
		});

		exception(BankException.class, (e, req, res) -> {
			res.status(400);
			res.body(toJson(new ResponseError(e)));
		});
		exception(Exception.class, (e, req, res) -> {
			res.status(500);
			res.body(toJson(new ResponseError(e)));
		});

		get("/stop", (req, res) -> {
			stop();
			return "OK";
		});
	}

	@Override
	public BankController getControllerBean() {
		return new BankController(getUserServiceBean(), getAccountServiceBean());
	}

	@Override
	public UserService getUserServiceBean() {
		return new UserServiceImpl(getStorageBean());
	}

	@Override
	public BankStorage getStorageBean() {
		if (bankStorage == null) {
			bankStorage = new BankStorageImpl();
		}
		return bankStorage;
	}

	@Override
	public AccountService getAccountServiceBean() {
		return new AccountServiceImpl(getStorageBean());
	}

	public String toJson(Object object) {
		return new Gson().toJson(object);
	}
}