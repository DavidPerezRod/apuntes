package com.zerti.utilities.message;

public enum MessageCodes implements ZertiMessageCode {

    //GENERIC ERRORS
    JSON_PARSING_ERROR("utilities.json.err.0001"),
    ASPECT_PROCEED_ERROR_LOG("utilities.json.err.0002"),
    REST_CONTROLLER_PARAM_PARSING_ERROR("utilities.http.rest.client.in.err.001"),

    //IBAN
    IBAN_NOT_FOUND("utilities.http.rest.client.in.err.IBAN.001"),
    IBAN_FORMAT_ERROR("utilities.http.rest.client.in.err.IBAN.002"),

    //HTTP REST CLIENTS RESPONSE ERROR
    HTTP_CLIENT_OUT_NO_RESPONSE_ERROR("utilities.http.rest.client.out.err.001"),
    HTTP_CLIENT_OUT_TIMEOUT_ERROR("utilities.http.rest.client.out.err.002"),
    HTTP_CLIENT_OUT_PROTOCOL_ERROR("utilities.http.rest.client.out.err.003"),

    //HTTP REST CLIENTS LOG ERROR
    HTTP_CLIENT_LOG_NO_RESPONSE_ERROR("utilities.http.rest.client.log.err.001"),
    HTTP_CLIENT_LOG_TIMEOUT_ERROR("utilities.http.rest.client.log.err.002"),
    HTTP_CLIENT_LOG_PROTOCOL_ERROR("utilities.http.rest.client.log.err.003");
	
	
    private final String messageCode;

    MessageCodes(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }
}
