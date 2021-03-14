package me.dabiz.exception.info.builder;

import me.dabiz.exception.info.category.http.error.HttpErrorCode;
import me.dabiz.exception.info.category.http.error.HttpErrorType;

public interface RestCategoryInfoBuilder {
    CategoryInfoBaseBuilder categoryInfo(HttpErrorType httpErrorType, HttpErrorCode httpErrorCode);
}
