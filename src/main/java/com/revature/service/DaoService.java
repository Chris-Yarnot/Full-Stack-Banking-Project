package com.revature.service;

import java.util.List;

import com.revature.database.DAOs;
import com.revature.models.dao.BankAccount;
import com.revature.models.dao.Transaction;
import com.revature.models.dao.User;
import com.revature.models.pres.AccountApplicationData;
import com.revature.models.pres.ApplicationAprovalResult;
import com.revature.models.pres.InvalidInputException;
import com.revature.models.pres.TransactionData;
import com.revature.models.web.BankAccountOptionEntry;

public interface DaoService {
	DAOs getDAOS();
	boolean handleApplyForBankAccount(AccountApplicationData aad, User u) throws InvalidInputException;
	boolean handleTransaction(TransactionData td, User u) throws InvalidInputException;
	
	List<BankAccount> getBankAccountsByUser(User u);
	List<Transaction> getTransactionsByUser(User u);
	List<Transaction> getTransactionsByAccount(BankAccount ba);

	List<BankAccountOptionEntry> getMyTransferToAccountOptions(User u);
	List<BankAccountOptionEntry> getOtherTransferToAccountOptions(User u);
	List<Transaction> getAllTransactions();
	BankAccount getMainAccountOfUser(User u);
	
	String getNameOfUser(User u);
	List<User> getAllUsers();
	List<BankAccountOptionEntry> getAccountsNeedingApproval();
	
	boolean updateAccountApprovalSetting(int accountId, ApplicationAprovalResult aar);
	
}
