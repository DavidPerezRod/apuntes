package me.dabiz.exception.info.body.base;

import me.dabiz.exception.info.Layer;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;

public class BusinessExceptionCategoryInfoBody extends CategoryInfoComposedBody {

    public BusinessExceptionCategoryInfoBody(final String message, final Layer layer){
        super(message, layer);
    }

    public BusinessExceptionCategoryInfoBody(final String message, final Layer layer, final SubcategoryInfo body){
        super(message, layer);
        addExceptionBodyLeaf(body);
    }
}
