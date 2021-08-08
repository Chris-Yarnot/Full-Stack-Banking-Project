package com.revature.database;

import com.revature.models.dao.AccountType;

public interface AccountTypeDAO extends GenericDAO<AccountType> {
	AccountType getByName(String s);
}
