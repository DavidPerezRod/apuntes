package com.zerti.utilities.aspects;

import com.zerti.utilities.log.aspect.RestClientLogAspect;
import com.zerti.utilities.log.service.RepositoryLogService;
import net.minidev.json.JSONObject;
import org.apache.commons.httpclient.methods.GetMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
class AspectLogTest {
	
	@InjectMocks
	RestClientLogAspect aspectLog;
	
	@Mock
	RepositoryLogService logService;
	
	ProceedingJoinPoint point;
	MethodSignature signature;
	Object[] args;
	JSONObject response;
	ResponseEntity responseEntity;

	@Before
	public void setUp() throws Exception {	
		point = mock(ProceedingJoinPoint.class);
		signature = mock(MethodSignature.class);
		responseEntity = new ResponseEntity("test", HttpStatus.OK);
		GetMethod get = new GetMethod();
		get.addRequestHeader("X-Request-ID", "request-id");
		args = new Object[] {get, "test"};
		response = new JSONObject();
	}

	@Test
	public void testRegisterPSD2() throws Throwable {
		when(point.getArgs()).thenReturn(args);
		when(point.getTarget()).thenReturn(args[0].getClass());
		when(point.proceed()).thenReturn(response);
		aspectLog.registerPSD2(point);
		verify(point, times(1)).proceed();
	}

	@Test
	public void testRegisterLog() throws Throwable {
		when(point.getArgs()).thenReturn(args);
		when(point.getTarget()).thenReturn(args[0].getClass());
		when(point.getSignature()).thenReturn(signature);
		when(point.getSignature().getName()).thenReturn("test");
		when(point.proceed()).thenReturn(responseEntity);
//		Assert.assertNotNull(aspectLog.registerLog(point));
	}
	
	@Test
	public void testRegisterLog_Request() throws Throwable {
		when(point.getArgs()).thenReturn(null);
		when(point.getTarget()).thenReturn(args[0].getClass());
		when(point.getSignature()).thenReturn(signature);
		when(point.getSignature().getName()).thenReturn("test");
		when(point.proceed()).thenReturn(responseEntity);
//		Assert.assertNotNull(aspectLog.registerLog(point));
	}
	
	@Test
	public void testRegisterLog_List() throws Throwable {
		when(point.getArgs()).thenReturn(args);
		when(point.getTarget()).thenReturn(args[0].getClass());
		when(point.getSignature()).thenReturn(signature);
		when(point.getSignature().getName()).thenReturn("test");
		responseEntity = new ResponseEntity(new ArrayList<>(Arrays.asList("test1", "test2")), HttpStatus.OK);
		when(point.proceed()).thenReturn(responseEntity);
//		Assert.assertNotNull(aspectLog.registerLog(point));
	}

	@Test
	public void testRegisterException() throws Throwable {
		when(point.getArgs()).thenReturn(args);
		when(point.getTarget()).thenReturn(args[0].getClass());
		when(point.getSignature()).thenReturn(signature);
		when(point.getSignature().getName()).thenReturn("test");
//		aspectLog.registerException(point);
		verify(point, times(1)).proceed();
	}

}
