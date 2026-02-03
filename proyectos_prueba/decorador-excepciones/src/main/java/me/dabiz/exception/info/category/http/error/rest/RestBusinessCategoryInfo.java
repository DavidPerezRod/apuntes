package me.dabiz.exception.info.category.http.error.rest;

import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;
import me.dabiz.exception.info.category.Category;
import me.dabiz.exception.info.scope.business.BusinessScopeCategoryInfo;

public class RestBusinessCategoryInfo extends BusinessScopeCategoryInfo {
    public RestBusinessCategoryInfo(Micro micro, CategoryInfo body, Category subcategoryInfo) {
        super(micro, body, subcategoryInfo);
    }
}
