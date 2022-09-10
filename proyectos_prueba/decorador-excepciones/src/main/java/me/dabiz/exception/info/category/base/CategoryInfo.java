package me.dabiz.exception.info.category.base;

import me.dabiz.exception.info.category.Layer;
import me.dabiz.exception.info.category.Category;

public class CategoryInfo extends ComposedCategoryInfo {

    public CategoryInfo(final String message, final Layer layer){
        super(message, layer);
    }

    public CategoryInfo(final String message, final Layer layer, final Category body){
        super(message, layer);
        addCategoryInfo(body);
    }
}
