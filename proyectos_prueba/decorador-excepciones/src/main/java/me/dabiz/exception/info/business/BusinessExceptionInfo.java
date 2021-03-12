package me.dabiz.exception.info.business;

import me.dabiz.exception.info.body.base.BusinessExceptionCategoryInfoBody;
import me.dabiz.exception.info.Category;
import me.dabiz.exception.info.Micro;

public final class BusinessExceptionInfo extends BusinessInfo<BusinessExceptionCategoryInfoBody>{

    public BusinessExceptionInfo(Micro micro, BusinessExceptionCategoryInfoBody body){
        super(micro, Category.BUSINESS_RULE, body);
    }
}
