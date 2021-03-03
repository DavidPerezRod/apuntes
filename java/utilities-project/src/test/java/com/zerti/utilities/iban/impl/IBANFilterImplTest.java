package com.zerti.utilities.iban.impl;

import com.zerti.utilities.iban.dominio.IBAN;
import com.zerti.utilities.iban.exception.IBANIncorrectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class IBANFilterImplTest {

	
	private final String ibanES = "ES7620770024003102575766";
	private final String ibanBE = "BE68539007547034";
	private final String ibanFR = "FR1420041010050500013M02606";
	private final String ibanUK = "GB29NWBK60161331926819";
	private final String ibanIE = "IE29AIBK93115212345678";
	
	private final int MIN_LENGTH_ES = 8;
	private final int MIN_LENGTH_BE = 7;
	private final int MIN_LENGTH_FR = 9;
	private final int MIN_LENGTH_UK = 8;
	private final int MIN_LENGTH_IE = 14;
	
	@InjectMocks
	IBANFilterImpl impl;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		impl = new IBANFilterImpl();
	}

	@Test
	void testGetIBANFiltered_ES() throws IBANIncorrectException{
		IBAN result = impl.getIBANFiltered(ibanES);
		assertEquals("ES", result.getCountryCode());
		assertEquals("76", result.getDc());
		assertEquals("2077", result.getBankCode());
		assertEquals("0024", result.getOfficeCode());
		assertEquals("00", result.getControlNumber());
		assertEquals("3102575766", result.getAccountNumber());
		assertNull(result.getSwiftCode());
		assertNull(result.getTypeAccountCode());
		assertEquals(ibanES, result.getIban());
	}
	
	@Test
	void testGetIBANFiltered_IE() throws IBANIncorrectException{
		IBAN result = impl.getIBANFiltered(ibanIE);
		assertEquals("IE", result.getCountryCode());
		assertEquals("29", result.getDc());
		assertEquals("AIBK", result.getSwiftCode());
		assertEquals("931152", result.getBankCode());
		assertEquals("12345678", result.getAccountNumber());
		assertNull(result.getControlNumber());
		assertNull(result.getTypeAccountCode());
		assertEquals(ibanIE, result.getIban());
	}

	@Test
	void testGetIBANFiltered_BE() throws IBANIncorrectException {
		IBAN result = impl.getIBANFiltered(ibanBE);
		assertEquals("BE", result.getCountryCode());
		assertEquals("68", result.getDc());
		assertEquals("539", result.getBankCode());
		assertEquals("0075470", result.getAccountNumber());
		assertEquals("34", result.getControlNumber());
		assertNull(result.getOfficeCode());
		assertNull(result.getSwiftCode());
		assertNull(result.getTypeAccountCode());
		assertEquals(ibanBE, result.getIban());
		
	}
	
	@Test
	void testGetIBANFiltered_FR() throws IBANIncorrectException{
		IBAN result = impl.getIBANFiltered(ibanFR);
		assertEquals("FR", result.getCountryCode());
		assertEquals("14", result.getDc());
		assertEquals("20041", result.getBankCode());
		assertEquals("01005", result.getOfficeCode());
		assertEquals("0500013M026", result.getAccountNumber());
		assertEquals("06", result.getControlNumber());
		assertNull(result.getSwiftCode());
		assertNull(result.getTypeAccountCode());
		assertEquals(ibanFR, result.getIban());
	}
	
	@Test
	void testGetIBANFiltered_UK() throws IBANIncorrectException{
		IBAN result = impl.getIBANFiltered(ibanUK);
		assertEquals("GB", result.getCountryCode());
		assertEquals("29", result.getDc());
		assertEquals("NWBK", result.getBankCode());
		assertEquals("601613", result.getTypeAccountCode());
		assertEquals("31926819", result.getAccountNumber());
		assertNull(result.getControlNumber());
		assertNull(result.getSwiftCode());
		assertNull(result.getOfficeCode());
		assertEquals(ibanUK, result.getIban());
	}
	
	@Test
	void testGetIBANFiltered_ES_Min() throws IBANIncorrectException{
		IBAN result = impl.getIBANFiltered(ibanES.substring(0, MIN_LENGTH_ES+1));
		assertEquals("ES", result.getCountryCode());
		assertEquals("76", result.getDc());
		assertEquals("2077", result.getBankCode());
		assertNull(result.getOfficeCode());
		assertNull(result.getControlNumber());
		assertNull(result.getAccountNumber());
		assertNull(result.getSwiftCode());
		assertNull(result.getTypeAccountCode());
		assertEquals(ibanES.substring(0, MIN_LENGTH_ES+1), result.getIban());
	}
	
	@Test
	void testGetIBANFiltered_IE_Min() throws IBANIncorrectException{
		IBAN result = impl.getIBANFiltered(ibanIE.substring(0, MIN_LENGTH_IE+1));
		assertEquals("IE", result.getCountryCode());
		assertEquals("29", result.getDc());
		assertEquals("AIBK", result.getSwiftCode());
		assertEquals("931152", result.getBankCode());
		assertNull(result.getAccountNumber());
		assertNull(result.getControlNumber());
		assertNull(result.getTypeAccountCode());
		assertEquals(ibanIE.substring(0, MIN_LENGTH_IE+1), result.getIban());
	}

	@Test
	void testGetIBANFiltered_BE_Min() throws IBANIncorrectException {
		IBAN result = impl.getIBANFiltered(ibanBE.substring(0, MIN_LENGTH_BE+1));
		assertEquals("BE", result.getCountryCode());
		assertEquals("68", result.getDc());
		assertEquals("539", result.getBankCode());
		assertNull(result.getAccountNumber());
		assertNull(result.getControlNumber());
		assertNull(result.getOfficeCode());
		assertNull(result.getSwiftCode());
		assertNull(result.getTypeAccountCode());
		assertEquals(ibanBE.substring(0, MIN_LENGTH_BE+1), result.getIban());
		
	}
	
	@Test
	void testGetIBANFiltered_FR_Min() throws IBANIncorrectException{
		IBAN result = impl.getIBANFiltered(ibanFR.substring(0, MIN_LENGTH_FR+1));
		assertAll("result",
				() -> assertEquals("FR", result.getCountryCode()),
				() -> assertEquals("14", result.getDc()),
				() -> assertEquals("20041", result.getBankCode()),
				() -> assertNull(result.getOfficeCode()),
				() -> assertNull(result.getAccountNumber()),
				() -> assertNull(result.getControlNumber()),
				() -> assertNull(result.getSwiftCode()),
				() -> assertNull(result.getTypeAccountCode()),
				() -> assertEquals(ibanFR.substring(0, MIN_LENGTH_FR+1), result.getIban())
		);
	}
	
	@Test
	void testGetIBANFiltered_UK_Min() throws IBANIncorrectException{
		IBAN result = impl.getIBANFiltered(ibanUK.substring(0, MIN_LENGTH_UK+1));
		assertAll("result",
				() -> assertEquals("GB", result.getCountryCode()),
				() -> assertEquals("29", result.getDc()),
				() -> assertEquals("NWBK", result.getBankCode()),
				() -> assertNull(result.getOfficeCode()),
				() -> assertNull(result.getAccountNumber()),
				() -> assertNull(result.getControlNumber()),
				() -> assertNull(result.getSwiftCode()),
				() -> assertNull(result.getTypeAccountCode()),
				() -> assertEquals(ibanUK.substring(0, MIN_LENGTH_UK+1), result.getIban())
		);
	}
	
	@Test()
	void testGetIBANFiltered_ES_Exception(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanES.substring(0, MIN_LENGTH_ES-1))
		);
	}
	
	@Test()
	void testGetIBANFiltered_IE_Exception(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanIE.substring(0, MIN_LENGTH_IE-1))
		);
	}

	@Test
	void testGetIBANFiltered_BE_Exception(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanBE.substring(0, MIN_LENGTH_BE-1))
		);
	}
	
	@Test
	void testGetIBANFiltered_FR_Exception(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanFR.substring(0, MIN_LENGTH_FR-1))
		);
	}
	
	@Test
	void testGetIBANFiltered_UK_Exception(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanUK.substring(0, MIN_LENGTH_UK-1))
		);
	}
	
	@Test()
	void testGetIBANFiltered_ES_ExceptionValid(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanES.concat("AA"))
		);
	}
	
	@Test()
	void testGetIBANFiltered_IE_ExceptionValid(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanIE.concat("AA"))
		);
	}

	@Test
	void testGetIBANFiltered_BE_ExceptionValid(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanBE.concat("AA"))
		);
	}
	
	@Test
	void testGetIBANFiltered_FR_ExceptionValid(){
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanFR.concat("AA"))
		);
	}
	
	@Test
	void testGetIBANFiltered_UK_ExceptionValid() {
		assertThrows(IBANIncorrectException.class, () ->
			impl.getIBANFiltered(ibanUK.concat("AA"))
		);
	}
	
}
