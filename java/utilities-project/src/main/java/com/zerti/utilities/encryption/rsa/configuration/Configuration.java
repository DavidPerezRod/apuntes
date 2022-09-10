package com.zerti.utilities.encryption.rsa.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class Configuration {

	private String certificate;
	
	private String secretKey;
	
	public Key getPrivateKey() throws CertificateException {
		try(InputStream is = new FileInputStream(new File(certificate))){
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			String pass = new String(Base64.decodeBase64(this.secretKey));
			keyStore.load(is, pass.toCharArray());
		
			Enumeration<String> aliases = keyStore.aliases();
			String alias = "";
			while(aliases.hasMoreElements()){
			    alias = aliases.nextElement();
			    if(keyStore.isKeyEntry(alias)){
			        break;
			    }
			}
			return keyStore.getKey(alias, pass.toCharArray());			
		} catch (UnrecoverableKeyException | KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new CertificateException(e.getMessage());
		}
	}

	
	public Key getPublicKey() throws CertificateException {
		try(InputStream is = new FileInputStream(new File(certificate))){
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			String pass = new String(Base64.decodeBase64(this.secretKey));
			keyStore.load(is, pass.toCharArray());
		
			Enumeration<String> aliases = keyStore.aliases();
			String alias = "";
			while(aliases.hasMoreElements()){
			    alias = aliases.nextElement();
			    if(keyStore.isKeyEntry(alias)){
			        break;
			    }
			}
			X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
			return certificate.getPublicKey();
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new CertificateException(e.getMessage());
		}
	}

}
