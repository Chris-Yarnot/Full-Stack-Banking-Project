package com.revature.database;

import java.util.List;

import com.revature.models.dao.BankAccount;
import com.revature.models.dao.Transaction;
import com.revature.models.dao.User;

public interface TransactionDAO extends GenericDAO<Transaction> {
	List<Transaction> getFromUser(User u);
	
	List<Transaction> getFromBankAccount(BankAccount ba);
	
	Transaction handleTransaction(BankAccount ba_from, BankAccount ba_to, int transferAmmount);
	
}
