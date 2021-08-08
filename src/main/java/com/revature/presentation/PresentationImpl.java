package com.revature.presentation;

import com.revature.MainDriver;
import com.revature.models.dao.User;
import com.revature.models.web.TransferType;
import com.revature.service.AuthService;
import com.revature.service.AuthServiceImpl;
import com.revature.service.DaoService;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class PresentationImpl implements Presentation {
	DaoService daoService;
	private AuthController authController;
	private AccountController accountController;
	private TransactionListController transController; 
	public PresentationImpl(DaoService daoService) {
		this.daoService= daoService;
		AuthService authService= new AuthServiceImpl(daoService.getDAOS());
		authController= new AuthControllerImpl(authService);
		accountController= new AccountControllerImpl(daoService, authService);
		transController= new TransactionListControllerImpl(daoService, authService);
	}
	@Override
	public void run() {
		Javalin app = Javalin.create(
				config -> {
					config.addStaticFiles("/public");
				}
			).start(9000);
		
		//Login
		app.post(Links.LOGIN, ctx -> authController.login(ctx, false));
		app.post(Links.REGISTER, ctx -> authController.login(ctx, true));
		app.get(Links.LOGOUT, ctx -> authController.logout(ctx));
		app.get("", ctx -> authController.handleHomepageRidirect(ctx));
		app.get("index.html", ctx -> authController.handleHomepageRidirect(ctx));
		//Bank Account Management
		app.get(Links.GET_MY_ACCOUNT, ctx -> accountController.getUserAccounts(ctx, false));
		app.get(Links.GET_OTHER_ACCOUNT, ctx -> accountController.getUserAccounts(ctx, true));
		app.get(Links.GET_APPLICATIONS, ctx -> accountController.getAccountsToApprove(ctx));
		app.post(Links.DEPOSIT, ctx -> accountController.handleTransfer(ctx, TransferType.DEPOSIT));
		//app.post(Links.WITHDRAW, ctx -> accountController.handleTransfer(ctx, TransferType.WITHDRAW));
		app.post(Links.TRANSFER, ctx -> accountController.handleTransfer(ctx, TransferType.TRANSFER));
		app.post(Links.CREATE_APPLICATION, ctx -> accountController.handleApplication(ctx));
		app.post(Links.ACCOUNT_APPROVAL, ctx -> accountController.handleAproval(ctx));
		
		//view transactions
		app.post(Links.GET_TRANSACTIONS, ctx -> transController.getTransactions(ctx));
		app.get(Links.GET_ALL_TRANSACTIONS, ctx -> transController.getTransactions(ctx));
	}
	
}
