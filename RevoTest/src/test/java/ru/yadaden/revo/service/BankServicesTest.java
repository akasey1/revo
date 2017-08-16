package ru.yadaden.revo.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.yadaden.revo.model.BankException;
import ru.yadaden.revo.storage.BankStorage;
import ru.yadaden.revo.storage.BankStorageImpl;

public class BankServicesTest {

	private static final String USERNAME = "USER";
	private static final int threads = 5;
	private static final int tasks = 1000;

	private BankStorage bankStorage;
	private UserService userService;
	private AccountService accountService;

	@Before
	public void cleanUpBeforeTest() {
		bankStorage = new BankStorageImpl();
		userService = new UserServiceImpl(bankStorage);
		accountService = new AccountServiceImpl(bankStorage);
	}

	@Test
	public void testTransfer() throws BankException {
		userService.createUser(USERNAME);
		String account1 = accountService.createAccount(USERNAME).getAccountNumber();
		String account2 = accountService.createAccount(USERNAME).getAccountNumber();

		accountService.addMoney(account1, 500);
		accountService.transfer(account1, account2, 300);

		Assert.assertEquals(bankStorage.accountByNumber(account1).getAmount(), 200);
		Assert.assertEquals(bankStorage.accountByNumber(account2).getAmount(), 300);
	}

	@Test
	public void testMultithreadTransfer() throws BankException, InterruptedException {
		userService.createUser(USERNAME);
		String account1 = accountService.createAccount(USERNAME).getAccountNumber();
		String account2 = accountService.createAccount(USERNAME).getAccountNumber();
		String account3 = accountService.createAccount(USERNAME).getAccountNumber();

		int startAmount = 100 * tasks;
		accountService.addMoney(account1, startAmount);

		ExecutorService pool = Executors.newFixedThreadPool(threads);
		for (int i = 0; i < tasks; i++) {
			pool.submit(() -> {
				try {
					accountService.transfer(account1, account2, 100);
					accountService.transfer(account1, account3, 100);
					accountService.transfer(account2, account3, 50);
					accountService.transfer(account3, account1, 150);
					accountService.transfer(account2, account1, 50);
				} catch (BankException e) {
					Assert.fail(e.getMessage());
				}
			});
		}
		pool.shutdown();
		pool.awaitTermination(60, TimeUnit.SECONDS);

		Assert.assertEquals(startAmount, bankStorage.accountByNumber(account1).getAmount());
		Assert.assertEquals(0, bankStorage.accountByNumber(account2).getAmount());
		Assert.assertEquals(0, bankStorage.accountByNumber(account3).getAmount());
	}

	@Test
	public void testMultithreadCreate() throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(threads);
		for (int i = 0; i < tasks; i++) {
			final int j = i;
			pool.submit(() -> {
				userService.createUser(USERNAME);
				userService.createUser(USERNAME + j);
				accountService.createAccount(USERNAME);
				accountService.createAccount(USERNAME + j);
				accountService.createAccount(USERNAME);
				accountService.createAccount(USERNAME + j);
			});
		}
		pool.shutdown();
		while (!pool.awaitTermination(60, TimeUnit.SECONDS))
			;

		Assert.assertNotNull(bankStorage.userByName(USERNAME));
		Assert.assertEquals(1 + tasks, userService.getAllUsers().size());
		Assert.assertEquals(tasks * 2, bankStorage.userByName(USERNAME).getAccounts().get().size());

		for (int i = 0; i < tasks; i++) {
			Assert.assertNotNull(bankStorage.userByName(USERNAME + i));
			Assert.assertEquals(2, bankStorage.userByName(USERNAME + i).getAccounts().get().size());
		}
	}
}
