package me.dabiz.exception.info.business;

import me.dabiz.exception.info.Category;
import me.dabiz.exception.info.Micro;
import me.dabiz.exception.info.body.base.BusinessExceptionCategoryInfoBody;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;

public class BusinessExceptionInfoHeritable extends BusinessInfo<BusinessExceptionCategoryInfoBody>{

    public BusinessExceptionInfoHeritable(Micro micro, BusinessExceptionCategoryInfoBody body, SubcategoryInfo subcategoryInfo) {
        super(micro, Category.PROGRAMMING, body);
        body.addExceptionBodyLeaf(subcategoryInfo);
    }
}
