package ru.yadaden.revo.model;

public class BankException extends Exception {

	private static final long serialVersionUID = 8768396131600515845L;

	public BankException(String msg) {
		super(msg);
	}
}