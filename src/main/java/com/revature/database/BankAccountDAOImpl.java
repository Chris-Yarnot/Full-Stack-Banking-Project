package com.revature.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.dao.BankAccount;
import com.revature.models.dao.User;
import com.revature.util.ConnectionFactory;

public class BankAccountDAOImpl extends GenericDAOImpl<BankAccount> implements BankAccountDAO {
	private static final String[] params = 
			{"owner_user_id",
			"account_name",
			"account_type_id",
			"account_balance",
			"is_activated"};
	private static final String table = "bank_accounts";
	private static final String key = "bank_account_id";
	public BankAccountDAOImpl() {
		super(table, params, key);
	}
	@Override
	public List<BankAccount> getAccountsNeedingApproval() {
		List<BankAccount> databaseEntries = new ArrayList<>();
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename + 
					" WHERE (" + params[4] + " = false);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				databaseEntries.add(getFromResults(rs));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return databaseEntries;
	}
	@Override
	public List<BankAccount> getFromUser(User u) {
		List<BankAccount> databaseEntries = new ArrayList<>();
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename + 
					" WHERE (" + params[0] + " = ?) AND ("
					+ params[4] + " = true);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u.getPrimaryKey());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				databaseEntries.add(getFromResults(rs));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return databaseEntries;
	}
	@Override
	protected void prepareStatement(PreparedStatement ps, BankAccount t, int startIndex) throws SQLException {
		ps.setInt(	 startIndex + 0, t.getOwnerUserId());	//{"owner_user_id",
		ps.setString(startIndex + 1, t.getAccountName());	//"account_name",
		setNullableInt(ps, startIndex + 2, t.getAccountTypeId());	//"account_type_id",
		ps.setInt(	 startIndex + 3, t.getAccountBalance());//"account_balance",
		ps.setBoolean(startIndex +4, t.isActivated());		//"is_activated"};
	}
	@Override
	protected BankAccount getFromResults(ResultSet rs) throws SQLException {
		return new BankAccount(
				rs.getInt(key),
				rs.getInt(		params[0]),	//{"owner_user_id",
				rs.getString(	params[1]),	//"account_name",
				rs.getInt(		params[2]),	//"account_type_id",
				rs.getInt(		params[3]),	//"account_balance",
				rs.getBoolean(  params[4])	//"is_activated"};
				);
	}
	@Override
	public BankAccount getByName(String s, User u) {
		BankAccount ba = null;
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename + 
					" WHERE (" + params[0] + " = ?) AND ("//owner_user_id
					+ params[4] + " = true) AND ("//is_activated
					+ params[1] + " =?);";//account_name
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u.getPrimaryKey());
			ps.setString(2, s);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				ba= getFromResults(rs);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return ba;
	}
	@Override
	public BankAccount getPrimaryFromUser(User owner) {
		if(owner == null) {
			return null;
		}
		BankAccount ba= this.getById(owner.getMainAccountId());
		if(ba == null) {
			List<BankAccount> bas= this.getFromUser(owner);
			if(bas != null && bas.size()> 0) {
				return bas.get(0);
			}
		}
		return ba;
		
	}
}
