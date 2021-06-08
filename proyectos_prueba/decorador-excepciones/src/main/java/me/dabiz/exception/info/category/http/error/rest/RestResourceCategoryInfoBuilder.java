package me.dabiz.exception.info.category.http.error.rest;

import me.dabiz.exception.info.category.Layer;
import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.builder.CategoryInfoBaseBuilder;
import me.dabiz.exception.info.builder.ExceptionInfoBuilder;
import me.dabiz.exception.info.builder.MicroBuilder;
import me.dabiz.exception.info.builder.RestCategoryInfoBuilder;
import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.category.base.CategoryInfo;
import me.dabiz.exception.info.category.http.error.HttpErrorCode;
import me.dabiz.exception.info.category.http.error.HttpErrorType;

public class RestResourceCategoryInfoBuilder implements MicroBuilder, CategoryInfoBaseBuilder, RestCategoryInfoBuilder, ExceptionInfoBuilder {
    private CategoryInfo categoryInfoBodyBase;
    private Micro micro;
    private RestCategoryInfo subcategoryInfo;

    public static RestCategoryInfoBuilder newRestResourceExceptionInfo(){
        return new RestResourceCategoryInfoBuilder();
    }

    public static RestCategoryInfoBuilder newRestBusinessExceptionInfo(){
        return new RestResourceCategoryInfoBuilder();
    }


    public RestResourceCategoryInfoBuilder() {
    }

    public ExceptionInfoBuilder micro(Micro micro) {
        this.micro = micro;
        return this;
    }

    public MicroBuilder baseInfo(String message, Layer layer) {
        categoryInfoBodyBase = new CategoryInfo(message, layer);
        return this;
    }

    public RestResourceCategoryInfoBuilder categoryInfo(HttpErrorType httpErrorType, HttpErrorCode httpErrorCode) {
        subcategoryInfo = new RestCategoryInfo(httpErrorType, httpErrorCode);
        return this;
    }

    public ExceptionInfo build() {
        return new RestResourceCategoryInfo(micro, categoryInfoBodyBase, subcategoryInfo);
    }
}