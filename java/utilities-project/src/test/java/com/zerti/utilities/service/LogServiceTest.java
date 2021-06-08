package com.zerti.utilities.service;

import com.zerti.utilities.log.repository.LogRepository;
import com.zerti.utilities.log.service.RepositoryLogService;
import net.minidev.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {
	
	@InjectMocks
	RepositoryLogService logService;
	
	@Mock
	LogRepository logRepository;
	

	JSONObject json;	
	NameValuePair[] names;

	@Before
	public void setUp() throws Exception {
		names = new NameValuePair[] {new NameValuePair("name", "value")};
		json = new JSONObject().appendField("name", "value");
	}

//	@Test
//	public void testRegisterRequest() {
//		logService.registerRequest(names);
//		verify(logRepository).insert(Mockito.any(Log.class));
//	}
//
//	@Test
//	public void testRegisterResponse() {
//		logService.registerResponse(json);
//		verify(logRepository).insert(Mockito.any(Log.class));
//	}

}
