package com.revature.models.pres;

public class InvalidInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String description;
	public InvalidInputException(String description) {
		super();
		this.description = description;
	}
	
}
