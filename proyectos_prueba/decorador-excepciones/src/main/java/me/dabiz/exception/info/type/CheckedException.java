package me.dabiz.exception.info.type;

import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.category.Category;

public class CheckedException extends Exception implements ZertiException {

    public final ExceptionInfo info;

    public CheckedException(ExceptionInfo info) {
        this.info= info;
    }

    public CheckedException(final Throwable cause, ExceptionInfo info) {
        super(cause);
        this.info= info;
    }

    public void addZertiExceptionBodyLeaf(Category exceptionBodyLeaf){
        info.getInfo().addCategoryInfo(exceptionBodyLeaf);
    }

    @Override
    public ExceptionInfo getExceptionInfo() {
        return info;
    }
}
