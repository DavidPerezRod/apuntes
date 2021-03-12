package me.dabiz.exception.info.business;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import me.dabiz.exception.info.Category;
import me.dabiz.exception.info.Micro;
import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.body.base.CategoryInfoBody;

@JsonPropertyOrder({ "micro", "category", "body" })
abstract class BusinessInfo<B extends CategoryInfoBody> implements ExceptionInfo {

    private final CategoryInfoBody body;
    private final Micro micro;
    private final Category category;

    BusinessInfo(Micro micro, Category category, B body) {
        this.body=body;
        this.micro=micro;
        this.category=category;
    }

    public Micro getMicro(){return micro;}
    public Category getCategory(){return category;}
    public CategoryInfoBody getBody(){return body;}
}
