package me.dabiz.exception.info.builder;

import me.dabiz.exception.info.scope.Scope;
import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.category.Layer;
import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.scope.application.ProgrammingScopeInfo;
import me.dabiz.exception.info.scope.application.ResourceScopeInfo;
import me.dabiz.exception.info.scope.application.SecurityScopeInfo;
import me.dabiz.exception.info.category.base.CategoryInfo;
import me.dabiz.exception.info.scope.business.AuthorizationScopeInfo;
import me.dabiz.exception.info.scope.business.BusinessScopeInfo;

import static me.dabiz.exception.info.scope.Scope.*;

public class ScopeInfoBuilder implements MicroBuilder, CategoryInfoBaseBuilder, ExceptionInfoBuilder {
    private CategoryInfo categoryInfoBase;
    private Micro micro;
    private final Scope scope;

    public ScopeInfoBuilder(final Scope scope) {
        this.scope =scope;
    }

    public static CategoryInfoBaseBuilder newResourceExceptionInfo() {
        return new ScopeInfoBuilder(RESOURCE);
    }
    public static CategoryInfoBaseBuilder newProgrammingExceptionInfo() {
        return new ScopeInfoBuilder(PROGRAMMING);
    }
    public static CategoryInfoBaseBuilder newSecurityExceptionInfo() {
        return new ScopeInfoBuilder(SECURITY);
    }
    public static CategoryInfoBaseBuilder newAuthorizationExceptionInfo() {return new ScopeInfoBuilder(AUTHORIZATION);}
    public static CategoryInfoBaseBuilder newBusinessExceptionInfo() {
        return new ScopeInfoBuilder(BUSINESS_RULE);
    }

    public ExceptionInfoBuilder micro(Micro micro) {
        this.micro = micro;
        return this;
    }

    public MicroBuilder baseInfo(String message, Layer layer) {
        categoryInfoBase = new CategoryInfo(message, layer);
        return this;
    }

    public ExceptionInfo build() {
        switch (scope){
            case AUTHORIZATION:
                return new AuthorizationScopeInfo(micro, categoryInfoBase);
            case PROGRAMMING:
                return new ProgrammingScopeInfo(micro, categoryInfoBase);
            case SECURITY:
                return new SecurityScopeInfo(micro, categoryInfoBase);
            case BUSINESS_RULE:
                return new BusinessScopeInfo(micro, categoryInfoBase);
            default:
                return new ResourceScopeInfo(micro, categoryInfoBase);
        }
    }
}