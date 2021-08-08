package com.revature.presentation;

import com.revature.database.DAOs;
import com.revature.models.dao.User;
import com.revature.models.pres.InvalidInputException;
import com.revature.models.pres.LoginData;
import com.revature.service.AuthService;
import com.revature.service.AuthServiceImpl;

import io.javalin.http.Context;

public class AuthControllerImpl implements AuthController {
	public AuthControllerImpl(AuthService authService) {
		this.authService = authService;
	}
	private AuthService authService;

	@Override
	public void login(Context ctx, boolean isRegistering) {
		String username = ctx.formParam("username");
		String password = ctx.formParam("password");
		System.out.println(username);
		//System.out.println(user);
		try {
			LoginData ld= new LoginData();
			ld.username= username;
			ld.password= password;
			ld.isExiting= false;
			ld.isRegistering= isRegistering;
			User u= authService.authenticateUser(ld); 
			if(u == null){
				ctx.status(403);
				ctx.redirect("login.html");
			}else {

				ctx.cookieStore(Cookies.USER_TOKEN, authService.createToken(username));
				ctx.status(200);
				this.handleHomepageRidirect(ctx);
			}
				
		} catch (InvalidInputException e) {
			ctx.status(407);
			ctx.redirect(Links.LOGIN_FORM);
			System.out.println(e.description);
			//TODO give warning message
		} catch (Exception e) {
			
			ctx.status(407);
			ctx.redirect(Links.LOGIN_FORM);
		}
		

	}

	
	@Override
	public void logout(Context ctx) {
		ctx.clearCookieStore();
		System.out.println("hi!");
		ctx.redirect("login.html");
		
	}

	@Override
	public boolean checkUser(Context ctx) {
		
		return authService.validateToken(ctx.cookieStore("user"));
	}
	@Override
	public void handleHomepageRidirect(Context ctx) {
		User u= authService.getUserFromToken(ctx.cookieStore(Cookies.USER_TOKEN));
		if(u == null) {
			ctx.redirect(Links.LOGIN_FORM);
		}else if(u.getEmployeeId() != 0) {
			
			ctx.redirect(Links.EMPLOYEE_LANDING);
		}else {
			ctx.redirect(Links.LOGIN_LANDING);
		}
	}
}
