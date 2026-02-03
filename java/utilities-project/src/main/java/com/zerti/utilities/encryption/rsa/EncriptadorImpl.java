package com.zerti.utilities.encryption.rsa;

import java.security.GeneralSecurityException;


public class EncriptadorImpl {
	
	private EncriptadorImpl() {
		super();
	}
	
	public static String encriptar(String cadenaSinEncriptar) throws GeneralSecurityException {
		return new Encriptador().encriptar(cadenaSinEncriptar);
	}
	
	public static String desencriptar(String cadenaEncriptada) throws GeneralSecurityException {
		return new Encriptador().desencriptar(cadenaEncriptada);
	}

}
