package com.revature.models.dao;

import com.revature.models.pres.LoginData;

public class User extends GenericModel{
	public User(int userId, String username, String userPassword, int employeeId, int customerId, int mainAccountId) {
		super(userId);
		this.username = username;
		this.userPassword = userPassword;
		this.employeeId = employeeId;
		this.customerId = customerId;
		this.mainAccountId = mainAccountId;
	}
	public User(LoginData ld) {
		super(0);
		this.username= ld.username;
		this.userPassword= ld.password;
	}
	public String getUsername() {
		return username;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public int getMainAccountId() {
		return mainAccountId;
	}
	private String username;
	private String userPassword;
	private int employeeId;
	private int customerId;
	private int mainAccountId;
	@Override
	public String toString() {
		return username;
	}
	public void setCustomerData(Customer c) {
		customerId= c.getPrimaryKey();
		
	}
	public void setMainAccountId(int accountId) {
		mainAccountId= accountId;
		
	}
}
