package com.revature.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.dao.GenericModel;
import com.revature.util.ConnectionFactory;

public abstract class GenericDAOImpl<T extends GenericModel> implements GenericDAO<T> {
	protected String tablename;
	//protected String[] parameters;
	protected int paramCount;
	protected String primaryKeyName;
	protected String groupedParams, groupedValues, paramValsSplicedWithCommas, paramValsSplicedWithAND;
	public GenericDAOImpl(String tablename, String[] parameters, String primaryKeyName) {
		super();
		this.tablename = tablename;
		paramCount = parameters.length;
		this.primaryKeyName = primaryKeyName;
		this.groupedParams = "(";
		this.groupedValues = "(";
		this.paramValsSplicedWithCommas = "";
		this.paramValsSplicedWithAND = "((";
		boolean isFirst= true;
		for(String s : parameters) {
			if(isFirst) {
				isFirst= false;
			}else {
				groupedParams += " , ";
				groupedValues += " , ";
				paramValsSplicedWithCommas += " , ";
				paramValsSplicedWithAND += ") AND (";
			}
			groupedParams += s;
			groupedValues += "?";
			paramValsSplicedWithCommas += s + " = ?";

			paramValsSplicedWithAND += s + " = ?";
		}
		groupedParams += ")";
		groupedValues += ")";

		paramValsSplicedWithAND += "))";
	}
	protected void prepareStatement(PreparedStatement ps, T t) throws SQLException {
		prepareStatement(ps, t, 1);
	}
	protected abstract void prepareStatement(PreparedStatement ps, T t, int startIndex) throws SQLException;
	
	protected abstract T getFromResults(ResultSet rs) throws SQLException;
	
	@Override
	public boolean add(T t) {
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "INSERT INTO "+ tablename + " "+ groupedParams +
					" VALUES " + groupedValues + ";";
			PreparedStatement ps= conn.prepareStatement(sql);
			prepareStatement(ps, t);
			ps.executeUpdate();
			
			//Update t's primary key to match 
			sql= "SELECT MAX("+ primaryKeyName + ") FROM " + tablename +";";
			ps= conn.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			if(rs.next()) {
				t.setPrimaryKey(rs.getInt(1));
			}else {
				System.out.println("primary key was not set");
			}
			return true;
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<T> getAll() {
		List<T> databaseEntries = new ArrayList<>();
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			
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
	public T getById(int key) {
		T output= null;
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "SELECT * FROM " + tablename
					+ " WHERE " + primaryKeyName + " = ? ;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, key);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				output= getFromResults(rs);
			}
						
		}catch (SQLException e) {
			
		}
		
		return output;
	}

	@Override
	public boolean update(T t) {
		int output= 0;
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "UPDATE " + tablename 
					+ " SET " + paramValsSplicedWithCommas
					+ " WHERE " + primaryKeyName + " = ? ;";
			PreparedStatement ps = conn.prepareStatement(sql);
			prepareStatement(ps, t);
			ps.setInt(paramCount + 1, t.getPrimaryKey());
			output = ps.executeUpdate();
			
		}catch (SQLException e) {
			
		}
		return output > 0;
		
	}
	
	@Override
	public boolean delete(T t) {
		int output= 0;
		try(Connection conn = ConnectionFactory.getConnection()){
			String sql = "DELETE FROM " + tablename 
					+ " WHERE " + primaryKeyName + " = ? ;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getPrimaryKey());
			output = ps.executeUpdate();
			
		}catch (SQLException e) {
			
		}
		return output > 0;
		
	}
	protected static void setRefInt(PreparedStatement ps,  int index,GenericModel gm) throws SQLException {
		if(gm == null) {
			ps.setNull(index, java.sql.Types.INTEGER);
		}else {
			ps.setInt(index, gm.getPrimaryKey());
		}
	}
	protected static void setNullableInt(PreparedStatement ps,int index, int i) throws SQLException {
		if(i == 0) {
			ps.setNull(index, java.sql.Types.INTEGER);
		}else {
			ps.setInt(index, i);
		}
	}
}
