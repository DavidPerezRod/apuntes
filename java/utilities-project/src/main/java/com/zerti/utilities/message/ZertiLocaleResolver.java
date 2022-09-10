package com.zerti.utilities.message;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;

public class ZertiLocaleResolver {
	
	private ZertiLocaleResolver(){
		super();
	}
	

    public static Locale getLocaleFromWebRequest(WebRequest request) {
        Locale locale = Locale.getDefault();
        String accept = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (!Strings.isEmpty(accept)) {
            String[] acceptArr = accept.split("-");
            locale = new Locale(acceptArr[0]);
        }
        return locale;
    }
    
    public static Locale getLocaleFromWebRequest(HttpServletRequest request) {
        Locale locale = Locale.getDefault();
        String accept = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (!Strings.isEmpty(accept)) {
            String[] acceptArr = accept.split("-");
            locale = new Locale(acceptArr[0]);
        }
        return locale;
    }
}
