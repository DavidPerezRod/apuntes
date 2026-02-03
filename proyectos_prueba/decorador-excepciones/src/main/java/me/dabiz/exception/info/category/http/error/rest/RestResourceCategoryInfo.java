package me.dabiz.exception.info.category.http.error.rest;


import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.Category;
import me.dabiz.exception.info.category.base.CategoryInfo;
import me.dabiz.exception.info.scope.application.ResourceScopeCategoryInfo;

public class RestResourceCategoryInfo extends ResourceScopeCategoryInfo {

    public RestResourceCategoryInfo(Micro micro, CategoryInfo base, Category subcategoryInfo) {
        super(micro, base, subcategoryInfo);
    }
}
