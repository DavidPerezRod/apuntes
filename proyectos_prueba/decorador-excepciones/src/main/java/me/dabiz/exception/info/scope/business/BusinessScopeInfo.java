package me.dabiz.exception.info.scope.business;

import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.CategoryInfo;

import static me.dabiz.exception.info.scope.Scope.BUSINESS_RULE;

public final class BusinessScopeInfo extends DomainScopeInfo<CategoryInfo> {

    public BusinessScopeInfo(Micro micro, CategoryInfo categoryInfo){
        super(micro, BUSINESS_RULE, categoryInfo);
    }
}
