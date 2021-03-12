package me.dabiz.exception.info.application;

import me.dabiz.exception.info.body.base.ApplicationExceptionCategoryInfoBody;
import me.dabiz.exception.info.Category;
import me.dabiz.exception.info.Micro;

public final class ResourceExceptionInfo extends ApplicationInfo<ApplicationExceptionCategoryInfoBody> {

    public ResourceExceptionInfo(Micro micro, ApplicationExceptionCategoryInfoBody body){
        super(micro, Category.RESOURCE, body);
    }
}
