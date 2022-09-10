package me.dabiz.exception.info;

import me.dabiz.exception.info.scope.Micro;
import me.dabiz.exception.info.scope.Scope;
import me.dabiz.exception.info.category.base.ICategoryInfo;

public interface ExceptionInfo {
    Micro getMicro();
    Scope getScope();
    ICategoryInfo getInfo();
}
