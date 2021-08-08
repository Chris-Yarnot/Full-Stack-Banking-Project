package com.revature.models.dao;

public class AccountType extends GenericModel{
	
	public AccountType(int accountTypeId, String accountTypeName) {
		super(accountTypeId);
		this.accountTypeName = accountTypeName;
	}
	
	private String accountTypeName;
	
	public String getAccountTypeName() {
		return accountTypeName;
	}

	@Override
	public String toString() {
		return this.accountTypeName;
	}
	
}
