package com.zerti.utilities.rest.client;


import com.loycuspay.psd2.sign.bussiness.SignImpl;
import com.loycuspay.psd2.sign.exception.SignException;
import com.zerti.utilities.exception.category.Micro;
import com.zerti.utilities.exception.hierarchy.rest.builder.ZertiRestServiceExceptionBuilder;
import com.zerti.utilities.exception.hierarchy.rest.type.HttpClientTypeError;
import com.zerti.utilities.exception.hierarchy.rest.type.HttpServiceTypeError;
import com.zerti.utilities.message.ZertiMessageResolver;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.logging.log4j.util.Strings;
import org.jsslutils.extra.apachehttpclient.SslContextedSecureProtocolSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.Locale;

import static com.zerti.utilities.exception.hierarchy.rest.builder.ZertiRestClientExceptionBuilder.clientException;
import static com.zerti.utilities.exception.hierarchy.rest.builder.ZertiRestServiceExceptionBuilder.serviceException;
import static com.zerti.utilities.exception.hierarchy.rest.type.HttpServiceTypeError.INTERNAL_SERVER_ERROR;
import static com.zerti.utilities.message.MessageCodes.*;

//TODO duplicar el cliente para hacer dos ramas de tratamiento, una para checked y otra para unchecked
@Slf4j
@Service
public class RestClientImpl {

    public static boolean SIGNED_REQUEST=true;
    public static boolean NOT_SIGNED_REQUEST=false;
    private static final String HTTPS = "https";
    private Micro micro;
    
    private final ZertiMessageResolver zertiMessageResolver;

	@Autowired
	RestClientImpl(ZertiMessageResolver zertiMessageResolver){
		this.zertiMessageResolver=zertiMessageResolver;
	}

    public JSONObject call(HttpMethodBase method, boolean isSignedCommunications, Locale locale, Micro micro){
        this.micro=micro;
        JSONObject response = new JSONObject();
        try {
            HttpClient httpClient = new HttpClient();
            checkAndRegisterSign(isSignedCommunications);
            response = processCallResponse(httpClient.executeMethod(method), method);
        } catch (IOException e) {
            setTypeError(method, e, locale);
        } finally {
            if (method.hasBeenUsed()) method.releaseConnection();
        }
        return response;
    }

    public JSONObject call(HttpMethodBase method, boolean isSignedCommunications, Micro micro){
        this.micro=micro;
        JSONObject response = new JSONObject();
        try {
            HttpClient httpClient = new HttpClient();
            checkAndRegisterSign(isSignedCommunications);
            response = processCallResponse(httpClient.executeMethod(method), method);
        } catch (IOException e) {
            setTypeError(method, e, LocaleContextHolder.getLocale());
        } finally {
            if (method.hasBeenUsed()) method.releaseConnection();
        }
        return response;
    }


    private void checkAndRegisterSign(boolean isSignedCommunications){
        try {
            if (isSignedCommunications) {
                SignImpl sign = new SignImpl();
                SSLContext sslClientContext = sign.setTLS();
                SslContextedSecureProtocolSocketFactory secureProtocolSocketFactory = new SslContextedSecureProtocolSocketFactory(sslClientContext);
                Protocol.registerProtocol(HTTPS, new Protocol(HTTPS, (ProtocolSocketFactory) secureProtocolSocketFactory, HttpsURL.DEFAULT_PORT));
            }
        }catch (SignException e){
            throw serviceException(micro,INTERNAL_SERVER_ERROR, e.getMessage()).unchecked();
        }
    }

    private JSONObject processCallResponse(int responseCode, final HttpMethodBase method){
        try {
        	if(!HttpStatus.Series.SUCCESSFUL.equals(HttpStatus.Series.resolve(responseCode))){
                setTypeResponseError(responseCode, method.getResponseBodyAsString());
            }
        	if(!Strings.isEmpty(method.getResponseBodyAsString())) {
        		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        		return (JSONObject) parser.parse(method.getResponseBodyAsString());
        	} else {
        		return new JSONObject();
        	}
        }catch (IOException | ParseException e){
            throw serviceException(micro, INTERNAL_SERVER_ERROR,e.getMessage()).unchecked();
        }
    }

    private void setTypeResponseError(int responseCode, String message){
        switch (responseCode) {
            case 404:
                throw clientException(micro, HttpClientTypeError.NOT_FOUND,message).unchecked();
            case 500:
                throw serviceException(micro,INTERNAL_SERVER_ERROR,message).unchecked();
            default:
                throw serviceException(micro, HttpServiceTypeError.BAD_GATEWAY,message).unchecked();
        }
    }

    private void setTypeError(final HttpMethodBase method, final IOException exception, Locale locale){
        String response = "";
        if (exception instanceof NoHttpResponseException || exception instanceof ConnectException) {
            response = zertiMessageResolver.getMessage(HTTP_CLIENT_OUT_NO_RESPONSE_ERROR, locale);
            log.error(zertiMessageResolver.getMessage(HTTP_CLIENT_LOG_NO_RESPONSE_ERROR, locale), Arrays.deepToString(method.getRequestHeaders()));
        } else if (exception instanceof ConnectTimeoutException) {
            response = zertiMessageResolver.getMessage(HTTP_CLIENT_OUT_TIMEOUT_ERROR, locale);
            log.error(zertiMessageResolver.getMessage(HTTP_CLIENT_LOG_TIMEOUT_ERROR, locale), Arrays.deepToString(method.getRequestHeaders()));
        } else if (exception instanceof ProtocolException) {
            response = zertiMessageResolver.getMessage(HTTP_CLIENT_OUT_PROTOCOL_ERROR, locale);
            log.error(zertiMessageResolver.getMessage(HTTP_CLIENT_LOG_PROTOCOL_ERROR, locale), Protocol.getProtocol(HTTPS));
        }
        throw ZertiRestServiceExceptionBuilder.serviceException(micro, INTERNAL_SERVER_ERROR, response, exception).unchecked();
    }
}
