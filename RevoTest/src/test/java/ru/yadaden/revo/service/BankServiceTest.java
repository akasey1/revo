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

public class BankServiceTest {

	private static final String USERNAME = "USER";
	private BankStorage bankStorage;
	private BankService bankService;

	@Before
	public void cleanUpBeforeTest() {
		bankStorage = new BankStorageImpl();
		bankService = new BankServiceImpl(bankStorage);
	}

	@Test
	public void testTransfer() throws BankException {
		bankService.createUser(USERNAME);
		String account1 = bankService.createAccount(USERNAME).getAccountNumber();
		String account2 = bankService.createAccount(USERNAME).getAccountNumber();

		bankService.addMoney(account1, 500);
		bankService.transfer(account1, account2, 300);

		Assert.assertEquals(bankStorage.accountByNumber(account1).getAmount(), 200);
		Assert.assertEquals(bankStorage.accountByNumber(account2).getAmount(), 300);
	}

	@Test
	public void testMultithreadTransfer() throws BankException, InterruptedException {
		bankService.createUser(USERNAME);
		String account1 = bankService.createAccount(USERNAME).getAccountNumber();
		String account2 = bankService.createAccount(USERNAME).getAccountNumber();
		String account3 = bankService.createAccount(USERNAME).getAccountNumber();

		bankService.addMoney(account1, 50000);

		ExecutorService pool = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 200; i++) {
			pool.submit(() -> {
				try {
					bankService.transfer(account1, account2, 100);
					bankService.transfer(account1, account3, 100);
					bankService.transfer(account2, account3, 50);
					bankService.transfer(account3, account1, 150);
					bankService.transfer(account2, account1, 50);
				} catch (BankException e) {
					Assert.fail(e.getMessage());
				}
			});
		}
		pool.shutdown();
		pool.awaitTermination(60, TimeUnit.SECONDS);

		Assert.assertEquals(50000, bankStorage.accountByNumber(account1).getAmount());
		Assert.assertEquals(0, bankStorage.accountByNumber(account2).getAmount());
		Assert.assertEquals(0, bankStorage.accountByNumber(account3).getAmount());
	}

	@Test
	public void testMultithreadCreate() throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 100; i++) {
			final int j = i;
			pool.submit(() -> {
				bankService.createUser(USERNAME);
				bankService.createUser(USERNAME + j);
				bankService.createAccount(USERNAME);
				bankService.createAccount(USERNAME + j);
				bankService.createAccount(USERNAME);
				bankService.createAccount(USERNAME + j);
			});
		}
		pool.shutdown();
		pool.awaitTermination(60, TimeUnit.SECONDS);

		Assert.assertNotNull(bankStorage.userByName(USERNAME));
		Assert.assertEquals(200, bankStorage.userByName(USERNAME).getAccounts().get().size());

		for (int i = 0; i < 100; i++) {
			Assert.assertNotNull(bankStorage.userByName(USERNAME + i));
			Assert.assertEquals(2, bankStorage.userByName(USERNAME + i).getAccounts().get().size());
		}
	}
}
