package me.dabiz.exception.info.scope.application;

import me.dabiz.exception.info.scope.Scope;
import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;
import me.dabiz.exception.info.category.Category;

public class SecurityScopeCategoryInfo extends ApplicationScopeInfo<CategoryInfo> {

    public SecurityScopeCategoryInfo(Micro micro, CategoryInfo body, Category category) {
        super(micro, Scope.PROGRAMMING, body);
        body.addCategoryInfo(category);
    }
}
