package com.revature.models.dao;

import com.revature.MainDriver;
import com.revature.util.Money;

public class Transaction extends GenericModel{
	public Transaction(int transactionId,  int accountFromId, int accountToId,
			int transferAmmount) {
		super(transactionId);
		this.transferAmmount = transferAmmount;
		this.accountFromId = accountFromId;
		this.accountToId = accountToId;
	}
	public Transaction( int accountFromId, int accountToId, int transferAmmount) {
		super(0);
		this.transferAmmount = transferAmmount;
		this.accountFromId = accountFromId;
		this.accountToId = accountToId;
		
	}
	public Transaction(BankAccount ba_from, BankAccount ba_to, int transferAmmount) {
		this((ba_from== null)?0:ba_from.getPrimaryKey(), (ba_to== null)?0:ba_to.getPrimaryKey(), transferAmmount);
	}
	private int transferAmmount;
	private int accountFromId;
	private int accountToId;
	

	public int getTransferAmmount() {
		return transferAmmount;
	}
	public int getAccountFromId() {
		return accountFromId;
	}
	public int getAccountToId() {
		return accountToId;
	}
	private BankAccount getAccountFrom() {
		return MainDriver.daos.bankAccounts.getById(accountFromId);
	}
	private BankAccount getAccountTo() {
		return MainDriver.daos.bankAccounts.getById(accountToId);
	}
	@Override
	public String toString() {
		return Money.getStringFromMoney(transferAmmount) +
				"\tfrom " + BankAccount.toString(getAccountFrom()) + 
				"\tto " + BankAccount.toString(getAccountTo());
		
	}
}
