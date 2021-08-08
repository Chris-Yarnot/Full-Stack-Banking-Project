package com.revature.models.pres;

import java.util.List;

import com.revature.models.web.BankAccountOptionEntry;

public class TransferToAccountOptions {
	List<BankAccountOptionEntry> usersAccounts;
	List<BankAccountOptionEntry> otherUsers;
	public TransferToAccountOptions() {
		super();
	}
	public TransferToAccountOptions(List<BankAccountOptionEntry> usersAccounts, List<BankAccountOptionEntry> otherUsers) {
		super();
		this.usersAccounts = usersAccounts;
		this.otherUsers = otherUsers;
	}
}
