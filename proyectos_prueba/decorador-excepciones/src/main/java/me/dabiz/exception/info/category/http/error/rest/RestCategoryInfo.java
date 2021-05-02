package me.dabiz.exception.info.category.http.error.rest;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import me.dabiz.exception.info.category.Category;
import me.dabiz.exception.info.category.http.error.HttpErrorCode;
import me.dabiz.exception.info.category.http.error.HttpErrorType;

@JsonPropertyOrder({ "category", "code", "status"})
public class RestCategoryInfo implements Category {

    private final String status;
    private final HttpErrorType httpErrorType;
    private int code;

    public RestCategoryInfo(HttpErrorType httpErrorType, HttpErrorCode serviceTypeError){
        this.httpErrorType = httpErrorType;
        this.code=serviceTypeError.getHttpStatusCode();
        this.status=serviceTypeError.getHttpStatusDescription();
    }

    @Override
    public HttpErrorType getCategory() {
        return httpErrorType;
    }

    public int getCode(){
        return code;
    }

    public String getStatus()
    {
        return status;
    }
}