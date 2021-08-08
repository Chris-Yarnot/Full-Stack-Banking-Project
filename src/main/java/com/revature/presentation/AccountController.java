package com.revature.presentation;

import com.revature.models.web.TransferType;

import io.javalin.http.Context;

public interface AccountController {
	
	void getUserAccounts(Context ctx, boolean others);
	void handleTransfer(Context ctx, TransferType tt);
	void handleApplication(Context ctx);
	
	void getAccountsToApprove(Context ctx);
	void handleAproval(Context ctx);
}
