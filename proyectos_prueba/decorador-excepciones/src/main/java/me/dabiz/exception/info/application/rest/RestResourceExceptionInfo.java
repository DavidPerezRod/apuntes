package me.dabiz.exception.info.application.rest;

import me.dabiz.exception.info.Micro;
import me.dabiz.exception.info.application.ResourceExceptionInfoHeritable;
import me.dabiz.exception.info.body.base.ApplicationExceptionCategoryInfoBody;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;

public class RestResourceExceptionInfo extends ResourceExceptionInfoHeritable {

    public RestResourceExceptionInfo(Micro micro, ApplicationExceptionCategoryInfoBody body, SubcategoryInfo subcategoryInfo) {
        super(micro, body, subcategoryInfo);
    }
}
