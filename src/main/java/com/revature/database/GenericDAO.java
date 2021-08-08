package com.revature.database;

import java.util.List;

import com.revature.models.dao.GenericModel;

public interface GenericDAO<T extends GenericModel> {
	//create
	boolean add(T t);
	//Read
	List<T> getAll();
	T getById(int id);
	//Update
	boolean update(T t);
	//Delete
	boolean delete(T t);
}
