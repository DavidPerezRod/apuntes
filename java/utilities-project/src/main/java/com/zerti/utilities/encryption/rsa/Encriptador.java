package com.zerti.utilities.encryption.rsa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.Cipher;

import com.zerti.utilities.encryption.rsa.configuration.Configuration;
import com.zerti.utilities.encryption.rsa.configuration.MapperConfiguration;



public class Encriptador {
	
	private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

	public Encriptador() {
		super();
	}
	
	private static Configuration getConfiguration() throws IOException {
		MapperConfiguration config = new MapperConfiguration();
		return config.getConfiguration();
	}
	
	public static Key getPrivateKey() throws CertificateException {
		try {
			Configuration config = getConfiguration();
			return config.getPrivateKey();
		} catch (IOException e) {
			throw new CertificateException(e.getMessage());
		}
	}
	
	public static Key getPublicKey() throws CertificateException {
		try {
			Configuration config = getConfiguration();
			return config.getPublicKey();
		} catch (IOException e) {
			throw new CertificateException(e.getMessage());
		}
	}
		
	protected String encriptar(String datos) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);        
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey());

        byte[] datosEncriptar = datos.getBytes(StandardCharsets.UTF_8);
        byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
        return Base64.getEncoder().encodeToString(bytesEncriptados);

    }
	
	protected String desencriptar(String datosEncriptados) throws GeneralSecurityException{
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey());
        
        byte[] bytesEncriptados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
       return new String(datosDesencriptados);

    }

}
