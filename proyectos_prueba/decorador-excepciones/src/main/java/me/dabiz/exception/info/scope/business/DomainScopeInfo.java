package me.dabiz.exception.info.scope.business;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import me.dabiz.exception.info.scope.Scope;
import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.category.base.ICategoryInfo;

@JsonPropertyOrder({ "micro", "scope", "info" })
abstract class DomainScopeInfo<B extends ICategoryInfo> implements ExceptionInfo {

    private final ICategoryInfo info;
    private final Micro micro;
    private final Scope scope;

    DomainScopeInfo(Micro micro, Scope scope, B info) {
        this.info =info;
        this.micro=micro;
        this.scope =scope;
    }

    public Micro getMicro(){return micro;}
    public Scope getScope(){return scope;}
    public ICategoryInfo getInfo(){return info;}
}
