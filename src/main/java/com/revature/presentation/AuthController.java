package com.revature.presentation;

import io.javalin.http.Context;

public interface AuthController {
	
	
	public void login(Context ctx, boolean isRegistering);
	
	public void logout(Context ctx);
	
	public boolean checkUser(Context ctx);

	void handleHomepageRidirect(Context ctx);

}
