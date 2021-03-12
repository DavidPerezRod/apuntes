package me.dabiz.exception.info.body.subcategory.http.rest;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;
import me.dabiz.exception.info.body.subcategory.http.HttpSubcategoryInfo;

@JsonPropertyOrder({ "type", "status", "code"})
public class RestSubcategoryInfo implements SubcategoryInfo {

    private final String status;
    public final String type;
    public int code;

    public RestSubcategoryInfo(String subcategory, HttpSubcategoryInfo serviceTypeError){
        this.type=subcategory;
        this.code=serviceTypeError.getHttpStatusCode();
        this.status=serviceTypeError.getHttpStatusDescription();
    }

    @Override
    public String getType() {
        return type;
    }

    public int getCode(){
        return code;
    }

    public String getStatus()
    {
        return status;
    }
}