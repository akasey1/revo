package ru.yadaden.revo.controller;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import lombok.Getter;
import ru.yadaden.revo.app.TransfersApp;
import ru.yadaden.revo.model.BankAccount;
import ru.yadaden.revo.model.BankUser;
import spark.Spark;
import spark.utils.IOUtils;

public class RestApiTest {
	private static final int START_AMOUNT = 1000;
	private static final int TRANSFER_AMOUNT = 300;
	private static final String USERNAME = "JOHN";

	@BeforeClass
	public static void beforeClass() throws InterruptedException {
		TransfersApp.main(null);
		Thread.sleep(1000);
	}

	@AfterClass
	public static void afterClass() {
		Spark.stop();
	}

	@Test
	public void testFullCycleRestApi() {
		// no users at start
		ArrayList users = request("GET", "/users").json(ArrayList.class);
		Assert.assertEquals(0, users.size());

		// test add user
		BankUser user = request("POST", "/users?username=" + USERNAME).json(BankUser.class);
		Assert.assertEquals(USERNAME, user.getUserName());
		Assert.assertEquals(0, user.getAccounts().get().size());
		user = request("GET", "/user?username=" + USERNAME).json(BankUser.class);
		Assert.assertEquals(USERNAME, user.getUserName());
		Assert.assertEquals(0, user.getAccounts().get().size());
		users = request("GET", "/users").json(ArrayList.class);
		Assert.assertEquals(1, users.size());

		// test add accounts
		BankAccount acc1 = request("POST", "/account?username=" + USERNAME).json(BankAccount.class);
		Assert.assertEquals(0, acc1.getAmount());
		BankAccount acc2 = request("POST", "/account?username=" + USERNAME).json(BankAccount.class);
		Assert.assertEquals(0, acc2.getAmount());
		Assert.assertNotEquals(acc1.getAccountNumber(), acc2.getAccountNumber());

		// test user has added accounts
		user = request("GET", "/user?username=" + USERNAME).json(BankUser.class);
		Assert.assertEquals(USERNAME, user.getUserName());
		Assert.assertEquals(2, user.getAccounts().get().size());
		Assert.assertEquals(Arrays.asList(acc1, acc2), user.getAccounts().get());

		// test add money to acc
		BankAccount acc1Added = request("POST",
				"/account/add?account=" + acc1.getAccountNumber() + "&amount=" + START_AMOUNT).json(BankAccount.class);
		Assert.assertEquals(acc1.getAccountNumber(), acc1Added.getAccountNumber());
		Assert.assertEquals(START_AMOUNT, acc1Added.getAmount());

		// test transfer
		String transferResult = request("POST", "/account/transfer?from=" + acc1.getAccountNumber() + "&to="
				+ acc2.getAccountNumber() + "&amount=" + TRANSFER_AMOUNT).json(String.class);
		Assert.assertEquals("OK", transferResult);
		acc1 = request("GET", "/account?account=" + acc1.getAccountNumber()).json(BankAccount.class);
		Assert.assertEquals(START_AMOUNT - TRANSFER_AMOUNT, acc1.getAmount());
		acc2 = request("GET", "/account?account=" + acc2.getAccountNumber()).json(BankAccount.class);
		Assert.assertEquals(TRANSFER_AMOUNT, acc2.getAmount());
	}

	private TestResponse request(String method, String path) {
		try {
			URL url = new URL("http://localhost:4567" + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.connect();
			String body = IOUtils.toString(connection.getInputStream());
			return new TestResponse(connection.getResponseCode(), body);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}

	@Getter
	private static class TestResponse {
		private final String body;
		private final int status;

		public TestResponse(int status, String body) {
			this.status = status;
			this.body = body;
		}

		public <T> T json(Class<T> clazz) {
			return new Gson().fromJson(body, clazz);
		}
	}
}
