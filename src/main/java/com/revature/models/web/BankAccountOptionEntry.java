package com.revature.models.web;

import com.revature.models.dao.BankAccount;
import com.revature.models.dao.User;

public class BankAccountOptionEntry {
	//Its usually not safe to send private banking info over the internet
	//send this instead
	public BankAccountOptionEntry(BankAccount ba, String s, String attachedUsername) {
		this.id= ba.getPrimaryKey();
		this.name= s;
		this.attachedUsername= attachedUsername;
	}
	public BankAccountOptionEntry() {
		
	}
	public int id;
	public String name;
	public String attachedUsername;
}
