package com.revature.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.MainDriver;
import com.revature.models.dao.BankAccount;
import com.revature.models.dao.Transaction;
import com.revature.models.dao.User;
import com.revature.util.ConnectionFactory;



public class TransactionDAOImpl extends GenericDAOImpl<Transaction> implements TransactionDAO{
	private static final String[] params = {
			"account_credit_from_id",
			"account_debit_to_id",
			"transfer_ammount"
			};
	private static final String table = "transactions";
	private static final String key = "transaction_id";
    TransactionDAOImpl() {
		super(table, params, key);
	}
	@Override
	protected void prepareStatement(PreparedStatement ps, Transaction t, int startIndex) throws SQLException {
		setNullableInt(ps, startIndex + 0, t.getAccountFromId());
		setNullableInt(ps, startIndex + 1, t.getAccountToId());
		ps.setInt(startIndex + 2, t.getTransferAmmount());
	}
	@Override
	protected Transaction getFromResults(ResultSet rs) throws SQLException {
		return new Transaction(rs.getInt(key), 
				rs.getInt(params[0]),
				rs.getInt(params[1]),
				rs.getInt(params[2])
				);
	}
	@Override
	public List<Transaction> getFromUser(User u) {
		List<Transaction> databaseEntries = new ArrayList<>();
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "WITH tem AS (SELECT bank_account_id FROM bank_accounts WHERE owner_user_id = ?)"
					+ "SELECT * FROM transactions "
					+ "WHERE (account_credit_from_id IN (SELECT * FROM tem)) "
					+ "OR (account_debit_to_id IN (SELECT * FROM tem));";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, u.getPrimaryKey());
			// adjust ps if needed
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				databaseEntries.add(getFromResults(rs));
			}
			
		}catch (SQLException e) {
			
		}
		
		return databaseEntries;
	}
	@Override
	public List<Transaction> getFromBankAccount(BankAccount ba) {
		List<Transaction> databaseEntries = new ArrayList<>();
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "WITH tem AS (SELECT bank_account_id FROM bank_accounts WHERE owner_user_id = ?)"
					+ "SELECT * FROM transactions "
					+ "WHERE (account_credit_from_id = ? ) "
					+ "OR (account_debit_to_id = ?);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, ba.getPrimaryKey());
			ps.setInt(1, ba.getPrimaryKey());
			// adjust ps if needed
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				databaseEntries.add(getFromResults(rs));
			}
			
		}catch (SQLException e) {
			
		}
		
		return databaseEntries;
	}
	
	@Override
	public Transaction handleTransaction(BankAccount ba_from, BankAccount ba_to, int transferAmmount) {
		Transaction output = null;
		MainDriver.logger.info("Attempted transaction: "+ new Transaction(ba_from, ba_to, transferAmmount));
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "BEGIN;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			sql= "SELECT * FROM perform_transaction(?, ?, ?);";
			ps= conn.prepareStatement(sql);
			setRefInt(ps, 1, ba_from);
			setRefInt(ps, 2, ba_to);
			 ps.setInt(3, transferAmmount);
			// adjust ps if needed
			
			 ResultSet rs= ps.executeQuery();
			 ps= conn.prepareStatement("COMMIT;");
			 ps.execute();
			 if(rs.next()) {
				 output= this.getById(rs.getInt(1));
				 MainDriver.logger.info("Handled Transaction successfully"+output);
			 }else {
				 MainDriver.logger.warn("transaction failed");
			 }
		}catch (SQLException e) {
			MainDriver.logger.warn("transaction failed");
			//e.printStackTrace();
		}
		
		return output;
	}
	@Override
	public boolean add(Transaction t) {
		MainDriver.logger.warn("Attempted to add transaction "+t+
				"\n through add instead of handle transaction \n"
				+"If this is not durring a bank account application process this is wrong");
		MainDriver.logger.warn(t);
		if(!super.add(t)) {
			MainDriver.logger.warn("transaction failed");
			return false;
		}
		return true;
	}
}
