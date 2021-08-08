package com.revature.models.dao;

public class Customer extends GenericModel{
	private String customerName;
	public Customer(int primaryKey, String customerName) {
		super(primaryKey);
		this.customerName = customerName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String newName) {
		customerName= newName;
	}
	@Override
	public String toString() {
		return this.customerName;
	}
	
}
