package me.dabiz.exception.info.business;

import me.dabiz.exception.info.body.base.BusinessExceptionCategoryInfoBody;
import me.dabiz.exception.info.Category;
import me.dabiz.exception.info.Micro;

public final class AuthorizationExceptionInfo extends BusinessInfo<BusinessExceptionCategoryInfoBody>{

    AuthorizationExceptionInfo(Micro micro, BusinessExceptionCategoryInfoBody body){
        super(micro, Category.AUTHORIZATION, body);
    }
}
