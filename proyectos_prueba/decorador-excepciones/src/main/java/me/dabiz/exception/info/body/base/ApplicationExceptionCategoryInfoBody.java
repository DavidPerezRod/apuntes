package me.dabiz.exception.info.body.base;

import me.dabiz.exception.info.Layer;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;

public class ApplicationExceptionCategoryInfoBody extends CategoryInfoComposedBody {

    public ApplicationExceptionCategoryInfoBody(final String message, final Layer layer){
        super(message, layer);
    }

    public ApplicationExceptionCategoryInfoBody(final String message, final Layer layer, SubcategoryInfo body){
        super(message, layer);
        addExceptionBodyLeaf(body);
    }
}
