package com.tradingcompany.tradingapp.exceptions;

public class StockNotFoundException extends RuntimeException{
	public StockNotFoundException(String message) {
		super(message);
	}
}
