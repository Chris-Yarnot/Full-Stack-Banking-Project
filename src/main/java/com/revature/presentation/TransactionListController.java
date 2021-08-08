package com.revature.presentation;

import io.javalin.http.Context;

public interface TransactionListController {
	public void getTransactions(Context ctx);
}
