package me.dabiz.exception.info.scope.application;

import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;
import me.dabiz.exception.info.category.Category;

import static me.dabiz.exception.info.scope.Scope.PROGRAMMING;

public class ResourceScopeCategoryInfo extends ApplicationScopeInfo<CategoryInfo> {

    public ResourceScopeCategoryInfo(Micro micro, CategoryInfo body, Category category) {
        super(micro, PROGRAMMING, body);
        body.addCategoryInfo(category);
    }
}
