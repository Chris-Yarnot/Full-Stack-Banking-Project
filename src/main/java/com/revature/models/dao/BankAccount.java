package com.revature.models.dao;

import com.revature.MainDriver;
import com.revature.models.pres.AccountApplicationData;

public class BankAccount extends GenericModel {
	public BankAccount(int bankAccountId, int ownerUserId, String accountName, int accountTypeId,
			int accountBalance, boolean is_activated) {
		super(bankAccountId);
		this.ownerUserId = ownerUserId;
		this.accountName = accountName;
		this.accountTypeId = accountTypeId;
		this.accountBalance = accountBalance;
		this.activated= is_activated;
	}
	public BankAccount(User user, int accountType, String accountName, int startingBalance) {
		super(0);
		this.ownerUserId= user.getPrimaryKey();
		this.accountName= accountName;
		this.accountTypeId= accountType;
		this.accountBalance= startingBalance;
		this.activated= false;
	}
	public BankAccount(User user,AccountApplicationData aad) {
		this(user, aad.accountType, aad.accountName, aad.initialDeposit);
	}
	private int ownerUserId;
	private String accountName; 
	private int accountTypeId;
	private int accountBalance;
	private boolean activated;
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public int getAccountTypeId() {
		return accountTypeId;
	}
	public void setAccountTypeId(int accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
	public int getOwnerUserId() {
		return ownerUserId;
	}
	public int getAccountBalance() {
		return accountBalance;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated() {
		this.activated = true;
	}
	public User getOwner() {
		return MainDriver.daos.users.getById(ownerUserId);
	}
	@Override
	public String toString() {
		return getOwner() + ":" + accountName;
	}
	public static String toString(BankAccount ba) {
		if(ba == null) {
			return "external account";
		}
		return ba.toString();
	}
	
}
