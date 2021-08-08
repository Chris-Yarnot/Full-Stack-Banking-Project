package com.revature.models.dao;

public abstract class GenericModel {
	private int primaryKey;

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public GenericModel(int primaryKey) {
		super();
		this.primaryKey = primaryKey;
	}
	public abstract String toString();
	public static boolean areEqual(GenericModel a, GenericModel b) {
		// static is cool because we can do null checking as well
		
		if((a == null) || (b == null) ) {
			return a == b;
			//if one of them is null just compare it to the other
		}else {
			if(a.getClass() == b.getClass()) {
				return a.getPrimaryKey() == b.getPrimaryKey();	
			}
			return false;
		}
	}
}
