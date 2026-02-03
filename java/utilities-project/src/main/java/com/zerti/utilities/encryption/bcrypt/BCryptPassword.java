package com.zerti.utilities.encryption.bcrypt;

public class BCryptPassword {
	
	private BCryptPassword() {
		super();
	}
	
	public static String encoder(String password) {
		BCryptPasswordImpl impl = new BCryptPasswordImpl();
		return impl.encoder(password);
	}

	public static boolean check(String password, String passwordEncoded) {
		BCryptPasswordImpl impl = new BCryptPasswordImpl();
		return impl.check(password, passwordEncoded);
	}
}
