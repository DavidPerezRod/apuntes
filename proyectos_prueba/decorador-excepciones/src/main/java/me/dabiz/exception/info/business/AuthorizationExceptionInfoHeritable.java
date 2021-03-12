package me.dabiz.exception.info.business;

import me.dabiz.exception.info.Category;
import me.dabiz.exception.info.Micro;
import me.dabiz.exception.info.body.base.BusinessExceptionCategoryInfoBody;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;

public class AuthorizationExceptionInfoHeritable extends BusinessInfo<BusinessExceptionCategoryInfoBody> {

    public AuthorizationExceptionInfoHeritable(Micro micro, BusinessExceptionCategoryInfoBody body, SubcategoryInfo subcategoryInfo) {
        super(micro, Category.PROGRAMMING, body);
        body.addExceptionBodyLeaf(subcategoryInfo);
    }
}
