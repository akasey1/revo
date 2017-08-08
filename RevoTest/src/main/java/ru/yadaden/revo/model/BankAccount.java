package ru.yadaden.revo.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "accountNumber" })
@RequiredArgsConstructor
public class BankAccount implements Serializable {
	private final BankUser owner;
	/**
	 * Emulate auto-creating of account numbers
	 */
	private final String accountNumber = UUID.randomUUID().toString();
	private volatile long amount = 0;
}