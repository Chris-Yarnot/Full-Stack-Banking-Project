package com.revature.presentation;

import com.revature.models.dao.User;
import com.revature.models.web.TransactionDetails;

import java.util.ArrayList;
import java.util.List;

import com.revature.models.dao.BankAccount;
import com.revature.models.dao.Transaction;
import com.revature.service.AuthService;
import com.revature.service.DaoService;
import com.revature.util.Money;

import io.javalin.http.Context;

public class TransactionListControllerImpl implements TransactionListController {
	DaoService daoService;
	AuthService authService;
	public TransactionListControllerImpl(DaoService daoService, AuthService authService) {
		this.daoService= daoService;
		this.authService= authService;
	}

	@Override
	public void getTransactions(Context ctx) {
		User u= authService.getUserFromToken(ctx.cookieStore(Cookies.USER_TOKEN));
		String s= ctx.formParam("type");
		List<Transaction> ts; 
		if(s != null && s.equalsIgnoreCase("user")) {
			ts= daoService.getTransactionsByUser(u);
		}else if(u != null && u.getEmployeeId() != 0) {
			ts= daoService.getAllTransactions();
		}else {
			ctx.status(407);
			return;
		}
		List<TransactionDetails> tds= new ArrayList<TransactionDetails>();
		for(Transaction t : ts) {
			TransactionDetails td= new TransactionDetails();
			td.bankAccountFrom= getBankAccountNameById(t.getAccountFromId(), u);
			td.bankAccountTo= getBankAccountNameById(t.getAccountToId(), u);
			td.ammount= Money.getStringFromMoney(t.getTransferAmmount());
			tds.add(td);
		}
		ctx.json(tds);
	}
	private String getBankAccountNameById(int id, User u) {
		if(id == 0) {
			return "external account";
		}else {
			BankAccount ba= daoService.getDAOS().bankAccounts.getById(id);
			return ba.toString();
		}
	}

}
