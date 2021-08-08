package com.revature.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.dao.Employee;



public class EmployeeDAOImpl extends GenericDAOImpl<Employee> implements EmployeeDAO{
	private static final String[] params = {"employee_name"};
	private static final String table = "employees";
	private static final String key = "employee_id";
    EmployeeDAOImpl() {
		super(table, params, key);
	}
	@Override
	protected void prepareStatement(PreparedStatement ps, Employee t, int startIndex) throws SQLException {
		ps.setString(startIndex + 0, t.getEmployeeName());
	}
	@Override
	protected Employee getFromResults(ResultSet rs) throws SQLException {

		return new Employee(rs.getInt(key), 
				rs.getString(params[0]));
	}
}
