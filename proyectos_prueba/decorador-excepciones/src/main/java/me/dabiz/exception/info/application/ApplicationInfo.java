package me.dabiz.exception.info.application;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import me.dabiz.exception.info.Category;
import me.dabiz.exception.info.Micro;
import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.body.base.CategoryInfoComposedBody;

@JsonPropertyOrder({ "micro", "category", "body" })
abstract class ApplicationInfo<B extends CategoryInfoComposedBody> implements ExceptionInfo {

    private final CategoryInfoComposedBody body;
    private final Micro micro;
    private final Category category;

    ApplicationInfo(Micro micro, Category category, B body) {
        this.body=body;
        this.micro=micro;
        this.category=category;
    }

    public Micro getMicro(){return micro;}
    public Category getCategory(){return category;}
    public CategoryInfoComposedBody getBody(){return body;}
}
