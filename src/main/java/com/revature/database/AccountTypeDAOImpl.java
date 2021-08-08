package com.revature.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.dao.AccountType;
import com.revature.models.dao.GenericModel;
import com.revature.util.ConnectionFactory;

public class AccountTypeDAOImpl extends GenericDAOImpl<AccountType> implements AccountTypeDAO {
	private static final String[] params = {"account_type_name"};
	private static final String table = "account_types";
	private static final String key = "bank_account_type_id";
    AccountTypeDAOImpl() {
		super(table, params, key);
	}

	@Override
	public AccountType getByName(String s) {
		AccountType output= null;
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename
					+ " WHERE account_type_name = ? ;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, s);
			//we expect rows and columns back from our db 
			ResultSet rs = ps.executeQuery();
			
			//we want to convert those columns and rows into objects. 
			if(rs.next()) {
				output= getFromResults(rs);
			}
			
			
			
		}catch (SQLException e) {
			
		}
		
		return output;
	}

	@Override
	protected AccountType getFromResults(ResultSet rs) throws SQLException {
		
		return new AccountType(rs.getInt(key), 
				rs.getString(params[0]));
	}

	@Override
	protected void prepareStatement(PreparedStatement ps, AccountType t, int startIndex) throws SQLException {
		ps.setString(1, t.getAccountTypeName());//account_type_name
	}


}
