package me.dabiz.exception.info.category.http.error;

import org.springframework.http.HttpStatus;

public enum ClientTypeError implements HttpErrorCode {
    //no se han incluido los errores de cliente deprecados org.springframework.http.HttpStatus
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE),
    PROXY_AUTHENTICATION_REQUIRED(HttpStatus.PROXY_AUTHENTICATION_REQUIRED),
    CONFLIC(HttpStatus.CONFLICT),
    GONE(HttpStatus.GONE),
    LENGTH_REQUIRED(HttpStatus.LENGTH_REQUIRED),
    PRECONDITION_FAILED(HttpStatus.PRECONDITION_FAILED),
    URI_TOO_LONG(HttpStatus.URI_TOO_LONG),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    REQUESTED_RANGE_NOT_SATISFIABLE(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE),
    EXPECTATION_FAILED(HttpStatus.EXPECTATION_FAILED),
    I_AM_A_TEAPOT(HttpStatus.I_AM_A_TEAPOT),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY),
    LOCKED(HttpStatus.LOCKED),
    TOO_EARLY(HttpStatus.TOO_EARLY),
    UPGRADE_REQUIRED(HttpStatus.UPGRADE_REQUIRED),
    PRECONDITION_REQUIRED(HttpStatus.PRECONDITION_REQUIRED),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS),
    UNAVAILABLE_FOR_LEGAL_REASONS(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

    private final HttpStatus status;

    ClientTypeError(HttpStatus status) {
        this.status = status;
    }

    @Override
    public int getHttpStatusCode() {
        return this.status.value();
    }

    @Override
    public String getHttpStatusDescription() {
        return this.status.name();
    }
}
