package me.dabiz.exception.info.business.rest;

import me.dabiz.exception.info.Micro;
import me.dabiz.exception.info.business.BusinessExceptionInfoHeritable;
import me.dabiz.exception.info.body.base.BusinessExceptionCategoryInfoBody;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;

public class RestBusinessExceptionInfo extends BusinessExceptionInfoHeritable {
    public RestBusinessExceptionInfo(Micro micro, BusinessExceptionCategoryInfoBody body, SubcategoryInfo subcategoryInfo) {
        super(micro, body, subcategoryInfo);
    }
}
