package me.dabiz.exception.info.scope.application;

import me.dabiz.exception.info.scope.Scope;
import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;
import me.dabiz.exception.info.category.Category;

public class ProgrammingScopeCategoryInfo extends ApplicationScopeInfo<CategoryInfo> {

    public ProgrammingScopeCategoryInfo(Micro micro, CategoryInfo categoryInfo, Category category) {
        super(micro, Scope.PROGRAMMING, categoryInfo);
        categoryInfo.addCategoryInfo(category);
    }
}
