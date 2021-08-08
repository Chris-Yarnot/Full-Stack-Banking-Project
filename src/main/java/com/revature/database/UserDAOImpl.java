package com.revature.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.dao.Customer;
import com.revature.models.dao.Employee;
import com.revature.models.dao.User;
import com.revature.util.ConnectionFactory;



public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO{
	private static final String[] params = {
			"username", //0
			"user_password",//1
			"employee_id",//2
			"customer_id",//3
			"main_account_id"//4
			};
	private static final String table = "users";
	private static final String key = "user_id";
    UserDAOImpl() {
		super(table, params, key);
	}
	@Override
	public User getByUsername(String username, String pass) {
		User output= null;
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename
					+ " WHERE " + params[0] + " = ? AND " + params[1] + " = ? ;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, pass);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				output= getFromResults(rs);
			}
						
		}catch (SQLException e) {
			
		}
		
		return output;
	}
	@Override
	public User getByUsername(String username) {
		User output= null;
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename
					+ " WHERE " + params[0] + " = ? ;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				output= getFromResults(rs);
			}
						
		}catch (SQLException e) {
			
		}
		return output;
	}
	@Override
	protected void prepareStatement(PreparedStatement ps, User t, int startIndex) throws SQLException {
		ps.setString(startIndex + 0, t.getUsername());//"username",
		ps.setString(startIndex + 1, t.getUserPassword());//"user_password",
		setNullableInt(ps,startIndex + 2, t.getEmployeeId());//"employee_id",
		setNullableInt(ps,startIndex + 3, t.getCustomerId());//"customer_id",
		setNullableInt(ps,startIndex + 4, t.getMainAccountId());//"main_account_id"
	}
	@Override
	protected User getFromResults(ResultSet rs) throws SQLException {
		return new User(
				rs.getInt(key),
				rs.getString(params[0]),
				rs.getString(params[1]),
				rs.getInt(params[2]),
				rs.getInt(params[3]),
				rs.getInt(params[4])
				);
	}
	@Override
	public User getByCustomer(Customer c) {
		User output= null;
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename
					+ " WHERE " + params[3] + " = ? ;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, c.getPrimaryKey());
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				output= getFromResults(rs);
			}
						
		}catch (SQLException e) {
			
		}
		
		return output;
	}
	
}
