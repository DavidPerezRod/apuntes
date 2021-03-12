package me.dabiz.exception.info.body.subcategory.http;

public interface HttpSubcategoryInfo {

    public enum HttpSubcategorytype{
        CLIENT,
        REDIRECTION,
        SERVICE
    }

    int getHttpStatusCode();
    String getHttpStatusDescription();
}
