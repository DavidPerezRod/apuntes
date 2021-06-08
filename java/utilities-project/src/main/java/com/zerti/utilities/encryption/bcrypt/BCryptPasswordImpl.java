package com.zerti.utilities.encryption.bcrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;

public class BCryptPasswordImpl {
	
	private BCryptPasswordEncoder passwordEncoder;
	
	protected BCryptPasswordImpl() {
		passwordEncoder = new BCryptPasswordEncoder(BCryptVersion.$2A, 10);
	}
	
	protected String encoder(String password) {
		return passwordEncoder.encode(password);
	}
	
	protected boolean check(String password, String encoded) {
		return passwordEncoder.matches(password, encoded);
	}

}
