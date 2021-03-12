package me.dabiz.exception.type;

import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;

public class ZertiCheckedException extends Exception implements ZertiException {

    public final ExceptionInfo info;

    public ZertiCheckedException(ExceptionInfo info) {
        this.info= info;
    }

    public ZertiCheckedException(final Throwable cause, ExceptionInfo info) {
        super(cause);
        this.info= info;
    }

    public void addZertiExceptionBodyLeaf(SubcategoryInfo exceptionBodyLeaf){
        info.getBody().addExceptionBodyLeaf(exceptionBodyLeaf);
    }

    @Override
    public ExceptionInfo getExceptionInfo() {
        return info;
    }
}
