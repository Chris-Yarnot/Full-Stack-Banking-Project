package com.revature.presentation;

import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;

import com.revature.models.dao.BankAccount;
import com.revature.models.dao.User;
import com.revature.models.pres.AccountApplicationData;
import com.revature.models.pres.ApplicationAprovalResult;
import com.revature.models.pres.InvalidInputException;
import com.revature.models.pres.TransactionData;
import com.revature.models.web.BankAccountOptionEntry;
import com.revature.models.web.TransferType;
import com.revature.service.AuthService;
import com.revature.service.DaoService;
import com.revature.util.Money;

import io.javalin.http.Context;

public class AccountControllerImpl implements AccountController {
	DaoService daos;
	AuthService auth;
	public AccountControllerImpl(DaoService daos, AuthService authService) {
		this.daos= daos;
		this.auth= authService;
	}
	@Override
	public void getUserAccounts(Context ctx, boolean others) {
		User u= auth.getUserFromToken(ctx.cookieStore(Cookies.USER_TOKEN));
		List<BankAccountOptionEntry> baoes;
		if(others) {
			baoes= daos.getOtherTransferToAccountOptions(u);
		}else {
			baoes= daos.getMyTransferToAccountOptions(u);
		}
		ctx.json(baoes);

	}

	@Override
	public void handleTransfer(Context ctx, TransferType tt) {
		try {
			User u= auth.getUserFromToken(ctx.cookieStore(Cookies.USER_TOKEN));
			int myBankAccount= Integer.parseInt(ctx.formParam("myBankAccount"));
			TransactionData td= new TransactionData();
			td.bankAccountFrom= myBankAccount;
			td.ammount= Double.parseDouble(ctx.formParam("amount"));
			System.out.println(td.ammount);
			if(tt == TransferType.TRANSFER) {
				td.bankAccountTo= Integer.parseInt(ctx.formParam("otherBankAccount"));
			}else if(ctx.formParam("deposit") != null) {
				System.out.println("deposit");
				td.bankAccountFrom= 0;
				td.bankAccountTo= myBankAccount;
			}else {
				System.out.println("withdraw");
			}
			System.out.println("Successfully created transaction");
			if(daos.handleTransaction(td, u)) {
				ctx.status(201);
				ctx.redirect(Links.DEPOSIT_WITHDRAW_LANDING);
				System.out.println("Success");
				return;
			}
		}catch(InvalidInputException e) {
			System.out.println(e.description);
		}catch(Exception e) {
			e.printStackTrace();
		}
		ctx.status(403);
		ctx.redirect(Links.DEPOSIT_WITHDRAW_LANDING);
		
	}

	@Override
	public void handleApplication(Context ctx) {
		User u= auth.getUserFromToken(ctx.cookieStore(Cookies.USER_TOKEN));
		AccountApplicationData aad= new AccountApplicationData();
		aad.accountName= ctx.formParam("accountName");
		aad.accountType= Integer.parseInt(ctx.formParam("accountType"));
		aad.initialDeposit= Money.getMoneyFromString(ctx.formParam("deposit"));
		try {
			if(daos.handleApplyForBankAccount(aad, u)) {
				ctx.status(201);
				ctx.redirect(Links.APPLY_LANDING);
				return;
			}
		} catch (InvalidInputException e) {
			e.printStackTrace();
			ctx.status(407);
		}
		ctx.status(407);
		ctx.redirect(Links.APPLY_FAIL_LANDING);
		
	}

	@Override
	public void handleAproval(Context ctx) {
		User u= auth.getUserFromToken(ctx.cookieStore(Cookies.USER_TOKEN));
		if(u == null || u.getEmployeeId() == 0) {
			ctx.status(407);
			return;
		}
		int bankAccount= Integer.parseInt(ctx.formParam("BankAccount"));
		ApplicationAprovalResult aar= ApplicationAprovalResult.REJECTED;
		if(ctx.formParam("approved").equalsIgnoreCase("TRUE")) {
			aar= ApplicationAprovalResult.APPROVED;
		}
		
		daos.updateAccountApprovalSetting(bankAccount, aar);
		ctx.status(201);
	}
	@Override
	public void getAccountsToApprove(Context ctx) {
		User u= auth.getUserFromToken(ctx.cookieStore(Cookies.USER_TOKEN));
		if(u == null || u.getEmployeeId() == 0) {
			ctx.status(407);
			return;
		}
		ctx.json(daos.getAccountsNeedingApproval());
		ctx.status(201);
	}

}
