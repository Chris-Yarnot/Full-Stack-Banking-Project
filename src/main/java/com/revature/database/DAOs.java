package com.revature.database;


public class DAOs {
	public final AccountTypeDAO accountTypes= 	new AccountTypeDAOImpl();
	public final BankAccountDAO bankAccounts= 	new BankAccountDAOImpl();
	public final CustomerDAO customers= new CustomerDAOImpl();
	public final EmployeeDAO employees= new EmployeeDAOImpl();
	public final TransactionDAO transactions= 	new TransactionDAOImpl();
	public final UserDAO users= 					new UserDAOImpl();
}
