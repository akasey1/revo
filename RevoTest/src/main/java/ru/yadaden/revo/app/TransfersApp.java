package ru.yadaden.revo.app;

import static spark.Spark.get;

public class TransfersApp {
	public static void main(String[] args) {
		get("/hello", (req, res) -> "Hello World");
	}
}