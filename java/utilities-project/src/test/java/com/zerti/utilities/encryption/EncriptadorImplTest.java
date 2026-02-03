package com.zerti.utilities.encryption;

import static org.junit.jupiter.api.Assertions.*;

import java.security.GeneralSecurityException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.zerti.utilities.encryption.rsa.EncriptadorImpl;

class EncriptadorImplTest {

	@InjectMocks
    EncriptadorImpl encriptador;

	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);
	}

	@SuppressWarnings("static-access")
	@Test
	void testEncriptar() throws GeneralSecurityException {
		assertNotNull(encriptador.encriptar("Encriptar"));
	}

	@SuppressWarnings("static-access")
	@Test
	void testDesencriptar() throws GeneralSecurityException {
		String encriptado = encriptador.encriptar("Encriptar");
		assertNotNull(encriptador.desencriptar(encriptado));
	}

}
