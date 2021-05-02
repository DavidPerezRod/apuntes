package me.dabiz.exception.info.scope.application;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import me.dabiz.exception.info.scope.Scope;
import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.ComposedCategoryInfo;

@JsonPropertyOrder({ "micro", "scope", "info" })
abstract class ApplicationScopeInfo<B extends ComposedCategoryInfo> implements ExceptionInfo {

    private final ComposedCategoryInfo info;
    private final Micro micro;
    private final Scope scope;

    ApplicationScopeInfo(Micro micro, Scope scope, B info) {
        this.info =info;
        this.micro=micro;
        this.scope =scope;
    }

    public Micro getMicro(){return micro;}
    public Scope getScope(){return scope;}
    public ComposedCategoryInfo getInfo(){return info;}
}
