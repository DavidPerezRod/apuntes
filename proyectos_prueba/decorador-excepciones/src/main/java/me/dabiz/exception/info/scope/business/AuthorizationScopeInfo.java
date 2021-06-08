package me.dabiz.exception.info.scope.business;

import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;

import static me.dabiz.exception.info.scope.Scope.AUTHORIZATION;

public final class AuthorizationScopeInfo extends DomainScopeInfo<CategoryInfo> {

    public AuthorizationScopeInfo(Micro micro, CategoryInfo categoryInfo){
        super(micro, AUTHORIZATION, categoryInfo);
    }
}
