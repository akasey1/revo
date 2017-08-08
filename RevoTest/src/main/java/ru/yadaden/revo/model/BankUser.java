package ru.yadaden.revo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "userName" })
@RequiredArgsConstructor
public class BankUser implements Serializable {

	/**
	 * unique login of user
	 */
	private final String userName;
	private AtomicReference<List<BankAccount>> accounts = new AtomicReference<List<BankAccount>>(
			Collections.emptyList());

	/**
	 * non-blocking add account
	 * 
	 * @param account
	 *            to add
	 */
	public void addAccount(BankAccount account) {
		boolean success = false;
		do {
			List<BankAccount> current = accounts.get();
			ArrayList<BankAccount> toUpdate = new ArrayList<>(current);
			toUpdate.add(account);
			success = accounts.compareAndSet(current, toUpdate);
		} while (!success);
	}
}