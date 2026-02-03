package com.zerti.utilities.exception.hierarchy.rest;

import com.zerti.utilities.exception.ZertiExceptionBody;
import com.zerti.utilities.exception.category.ExceptionItem;
import com.zerti.utilities.exception.category.Micro;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ZertiRestExceptionBody implements ZertiExceptionBody {

    private final String message;
    private final HttpStatus status;
    private final Micro micro;
    private final ExceptionItem item;
}
