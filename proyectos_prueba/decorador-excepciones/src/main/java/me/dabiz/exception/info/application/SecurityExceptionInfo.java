package me.dabiz.exception.info.application;

import me.dabiz.exception.info.body.base.ApplicationExceptionCategoryInfoBody;
import me.dabiz.exception.info.Category;
import me.dabiz.exception.info.Micro;

public final class SecurityExceptionInfo extends ApplicationInfo<ApplicationExceptionCategoryInfoBody> {

    public SecurityExceptionInfo(Micro micro, ApplicationExceptionCategoryInfoBody body){
        super(micro, Category.SECURITY, body);
    }
}
