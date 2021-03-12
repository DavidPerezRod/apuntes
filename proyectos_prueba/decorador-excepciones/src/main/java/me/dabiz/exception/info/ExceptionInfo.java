package me.dabiz.exception.info;

import me.dabiz.exception.info.body.base.CategoryInfoBody;

public interface ExceptionInfo {
    Micro getMicro();
    Category getCategory();
    CategoryInfoBody getBody();
}
