package me.dabiz.exception.info.scope.application;

import me.dabiz.exception.info.scope.Scope;
import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;

public final class ResourceScopeInfo extends ApplicationScopeInfo<CategoryInfo> {

    public ResourceScopeInfo(Micro micro, CategoryInfo categoryInfo){
        super(micro, Scope.RESOURCE, categoryInfo);
    }
}
