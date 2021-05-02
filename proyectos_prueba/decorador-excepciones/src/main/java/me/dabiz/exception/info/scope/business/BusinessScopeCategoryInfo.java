package me.dabiz.exception.info.scope.business;

import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;
import me.dabiz.exception.info.category.Category;

import static me.dabiz.exception.info.scope.Scope.PROGRAMMING;

public class BusinessScopeCategoryInfo extends DomainScopeInfo<CategoryInfo> {

    public BusinessScopeCategoryInfo(Micro micro, CategoryInfo categoryInfo, Category category) {
        super(micro, PROGRAMMING, categoryInfo);
        categoryInfo.addCategoryInfo(category);
    }
}
