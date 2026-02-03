package me.dabiz.exception.info.scope.application;

import me.dabiz.exception.info.scope.Scope;
import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;

public final class ProgrammingScopeInfo extends ApplicationScopeInfo<CategoryInfo> {

    public ProgrammingScopeInfo(Micro micro, CategoryInfo categoryInfo) {
        super(micro, Scope.PROGRAMMING, categoryInfo);
    }
}
