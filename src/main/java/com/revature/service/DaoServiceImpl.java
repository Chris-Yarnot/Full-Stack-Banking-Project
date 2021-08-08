package com.revature.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ajax.JSON;

import com.revature.database.DAOs;
import com.revature.models.dao.BankAccount;
import com.revature.models.dao.Customer;
import com.revature.models.dao.Employee;
import com.revature.models.dao.GenericModel;
import com.revature.models.dao.Transaction;
import com.revature.models.dao.User;
import com.revature.models.pres.AccountApplicationData;
import com.revature.models.pres.ApplicationAprovalResult;
import com.revature.models.pres.InvalidInputException;
import com.revature.models.pres.TransactionData;
import com.revature.models.web.BankAccountOptionEntry;
import com.revature.util.Money;

public class DaoServiceImpl implements DaoService {
	DAOs daos;
	Logger log;
	
	public DaoServiceImpl(DAOs daos, Logger log) {
		super();
		this.daos = daos;
		this.log = log;
	}


	@Override
	public boolean handleApplyForBankAccount(AccountApplicationData aad, User u)
			throws InvalidInputException{
		if(aad == null || aad.canceled || aad.exitProgram) {
			return false;
		}else if(aad.initialDeposit < 0){
			throw new InvalidInputException("Initial Deposit should not be negative");
		}else {
			BankAccount ba= new BankAccount(u,aad);
			log.info("attempted to apply for account "+ ba);
			if(daos.bankAccounts.add(ba)) {
				return true;
			}else {
				log.warn("Apply for account failed for unknown reasons");
				return false;
			}
		}
	}

	@Override
	public boolean handleTransaction(TransactionData td, User u) throws InvalidInputException {
		if(td == null || td.canceled) {
			return false;
		}else if(td.exitProgram) {
			return true;
		}else if(td.ammount < 0){
			throw new InvalidInputException("You cannot transfer a negative ammount");
		}else if(td.ammount == 0) {
			throw new InvalidInputException("You cannot transfer a zero ammount");
		}else if(td.bankAccountFrom == td.bankAccountTo) {
			throw new InvalidInputException("You cannot transfer from an account to itself");
		}else {
			BankAccount accountFrom= daos.bankAccounts.getById(td.bankAccountFrom);
			BankAccount accountTo= daos.bankAccounts.getById(td.bankAccountTo);
			if(accountFrom == null && accountTo == null) {
				throw new InvalidInputException("There is no bank accounts with those ids");
			}
			int shouldBeOwnersId= (accountFrom != null)? accountFrom.getOwnerUserId() : accountTo.getOwnerUserId();
			if(shouldBeOwnersId != u.getPrimaryKey()) {
				throw new InvalidInputException("This bank account does not belong to the user");
			}
			Transaction t= daos.transactions.handleTransaction(
					accountFrom, accountTo, (int)(td.ammount * 100)); 
			if(t != null) {
				
				return true;
			}else {
				throw new InvalidInputException("An unknown Error occured");
				
			}
			
		}
	}

	@Override
	public List<BankAccount> getBankAccountsByUser(User u) {
		return daos.bankAccounts.getFromUser(u);
	}
	
	@Override
	public BankAccount getMainAccountOfUser(User u) {
		return daos.bankAccounts.getPrimaryFromUser(u);
	}
	
	@Override
	public List<User> getAllUsers(){
		return daos.users.getAll();
	}

	@Override
	public List<Transaction> getTransactionsByUser(User u) {
		return daos.transactions.getFromUser(u);
	}

	@Override
	public List<Transaction> getTransactionsByAccount(BankAccount ba) {
		return daos.transactions.getFromBankAccount(ba);
	}

	@Override
	public List<BankAccountOptionEntry> getOtherTransferToAccountOptions(User me) {
		List<BankAccountOptionEntry> baoes= new ArrayList<BankAccountOptionEntry>();
		for(User u : daos.users.getAll()) {
			if(GenericModel.areEqual(u, me)) {
				continue;
			}
			BankAccount ba= this.getMainAccountOfUser(u);
			if(ba == null) {
				continue;
			}
			String s= this.getNameOfUser(u);
			baoes.add(new BankAccountOptionEntry(ba, s, u.getUsername()));
		}
		return baoes;
	}
	@Override
	public List<BankAccountOptionEntry> getMyTransferToAccountOptions(User u) {
		List<BankAccountOptionEntry> baoes= new ArrayList<BankAccountOptionEntry>();
		//String userString= this.getNameOfUser(u);
		for(BankAccount ba : daos.bankAccounts.getFromUser(u)) {
			baoes.add(new BankAccountOptionEntry(ba,ba.getAccountName(), 
					Money.getStringFromMoney(ba.getAccountBalance())));
			
		}
		return baoes;
	}

	@Override
	public List<Transaction> getAllTransactions() {
		
		return daos.transactions.getAll();
	}

	@Override
	public List<BankAccountOptionEntry> getAccountsNeedingApproval() {
		List<BankAccountOptionEntry> baoes= new ArrayList<BankAccountOptionEntry>();
		for(BankAccount ba : daos.bankAccounts.getAccountsNeedingApproval()) {
			User u= daos.users.getById(ba.getOwnerUserId());
			String userString= Money.getStringFromMoney(ba.getAccountBalance());
			String s= ba.toString();
			baoes.add(new BankAccountOptionEntry(ba, s, userString));
		}
		return baoes;
	}

	@Override
	public String getNameOfUser(User u) {
		//Employee name is prioritized first if they have one
		int x= u.getEmployeeId();
		if(x != 0){
			Employee c= daos.employees.getById(x);
			if(c != null) {
				return c.getEmployeeName();
			}
		}
		//Customer name is prioritized second if they have one
		x= u.getCustomerId();
		if(x != 0){
			Customer c= daos.customers.getById(x);
			if(c != null) {
				return c.getCustomerName();
			}
		}
		//otherwise everyone should have a username
		return u.getUsername();
	}

	@Override
	public boolean updateAccountApprovalSetting(int accountId, ApplicationAprovalResult aar) {
		BankAccount ba= daos.bankAccounts.getById(accountId);
		switch(aar) {
		case APPROVED:
			log.info(ba+" is being Approved.");
			ba.setActivated();
			if(daos.bankAccounts.update(ba)) {
				if(ba.getAccountBalance() != 0) {
					daos.transactions.add(new Transaction(0, ba.getPrimaryKey(), ba.getAccountBalance()));
				}
				User owner= ba.getOwner();
				if(owner.getMainAccountId() == 0) {
					owner.setMainAccountId(ba.getPrimaryKey());
					daos.users.update(owner);
				}
				return true;
			}else {
				log.warn("account approval failed");
				return false;
			}
		case REJECTED:
			log.info(ba+" is being rejected.");
			if(daos.bankAccounts.delete(ba)) {
				return true;
			}else {
				log.error("rejection failed");
				return false;
			}
		case VIEW_LATER:
			return true;
		case CLOSE_MENU:
		case EXIT_PROGRAM:
			return false;
		}
		//this should be unreachable
		return false;
	}


	@Override
	public DAOs getDAOS() {
		return daos;
	}	


}
