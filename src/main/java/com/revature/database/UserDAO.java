package com.revature.database;

import com.revature.models.dao.Customer;
import com.revature.models.dao.User;

public interface UserDAO extends GenericDAO<User>{
	User getByUsername(String username, String pass);
	User getByUsername(String username);
	User getByCustomer(Customer c);
}
