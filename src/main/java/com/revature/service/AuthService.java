package com.revature.service;

import com.revature.models.dao.User;
import com.revature.models.pres.InvalidInputException;
import com.revature.models.pres.LoginData;

public interface AuthService {
	public User authenticateUser(LoginData ld) throws InvalidInputException;
	
	public String createToken(String username);
	
	public boolean validateToken(String token);


	public User getUserFromToken(String token);
}
