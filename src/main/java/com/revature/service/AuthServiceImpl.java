package com.revature.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import com.revature.database.DAOs;
import com.revature.database.UserDAO;
import com.revature.database.UserDAOImpl;
import com.revature.models.dao.User;
import com.revature.models.pres.InvalidInputException;
import com.revature.models.pres.LoginData;

public class AuthServiceImpl implements AuthService {
	
	private static byte[] salt = new SecureRandom().getSeed(16);
	
	
	private Map<String, String> tokenRepo = new HashMap<>();
	DAOs daos;
	public AuthServiceImpl(DAOs daos) {
		this.daos= daos;
	}
	@Override
	public User authenticateUser(LoginData ld) throws InvalidInputException {
		if(ld.isExiting) {
			return null;
		}else if(ld.isRegistering) {
			User user= new User(ld);
			if(daos.users.add(user)) { //this should attempt a login immediately after
				return user;
			}else {
				throw new InvalidInputException("This username was already taken.");
				//if registration fails
			}
		}else {
			User user= daos.users.getByUsername(ld.username);
			if(user == null) {
				throw new InvalidInputException("This username is not recognized");
			}else if(user.getUserPassword().equals(ld.password)) {
				return user;
			}else {
				throw new InvalidInputException("The password is wrong");
			}
		}
	}


	@Override
	public String createToken(String username) {
		System.out.println("created token");
		//TODO remove debug tag
		String token = simpleHash(username);
		tokenRepo.put(token, username);
		
		return token;
	}
	


	@Override
	public boolean validateToken(String token) {

		return tokenRepo.containsKey(token);
	}
	
	public User getUserFromToken(String token) {
		return daos.users.getByUsername(tokenRepo.get(token));
	}
	
	private String simpleHash(String username) {
		String hash = null;
		
		MessageDigest md;
		
		try {
			md = MessageDigest.getInstance("SHA-512");
			
			md.update(salt);
			
			byte[] bytes = md.digest(username.getBytes());
			
			StringBuilder sb = new StringBuilder();
			
			for(int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(0));
			}
			
			hash = sb.toString();
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		
		return hash;
	}
	
	

}
