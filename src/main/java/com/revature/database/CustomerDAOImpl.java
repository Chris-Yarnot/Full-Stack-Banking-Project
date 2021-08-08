package com.revature.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.dao.Customer;
import com.revature.models.dao.User;
import com.revature.util.ConnectionFactory;



public class CustomerDAOImpl extends GenericDAOImpl<Customer> implements CustomerDAO {
	private static final String[] params = {"customer_name"};
	private static final String table = "customers";
	private static final String key = "customer_id";
    CustomerDAOImpl() {
		super(table, params, key);
	}
	@Override
	protected void prepareStatement(PreparedStatement ps, Customer t, int startIndex) throws SQLException {
		ps.setString(startIndex + 0, t.getCustomerName());
	}
	@Override
	protected Customer getFromResults(ResultSet rs) throws SQLException {

		return new Customer(rs.getInt(key), 
				rs.getString(params[0]));
	}
	@Override
	public List<Customer> getByName(String s) {
		List<Customer> output= new ArrayList<>();
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename
					+ " WHERE " + params[0] + " = ? "
					+ "ORDER BY "+key + "desc;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, s);
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				output.add( getFromResults(rs));
			}
						
		}catch (SQLException e) {
			
		}
		
		return output;
	}
}
