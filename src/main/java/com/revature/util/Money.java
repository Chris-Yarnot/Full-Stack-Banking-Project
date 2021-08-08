package com.revature.util;

public class Money {
	public static int getMoneyFromString(String s) {
		if(s.charAt(0) == '$') {
			s= s.substring(1);
		}
		double d;
		try {
			d= Double.parseDouble(s);
		}catch(NumberFormatException nfe) {
			d= 0;
		}
		return (int) d*100;
	}
	public static String getStringFromMoney(int i) {
		return "$" + i/100 + "." + i/10%10 + i%10; 
	}
}
