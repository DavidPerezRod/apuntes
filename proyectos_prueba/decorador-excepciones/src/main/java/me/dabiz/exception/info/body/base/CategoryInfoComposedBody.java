package me.dabiz.exception.info.body.base;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import me.dabiz.exception.info.Layer;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;

import java.util.ArrayList;
import java.util.List;


@Data
@JsonPropertyOrder({ "layer", "message", "subcategoryInfo" })
public class CategoryInfoComposedBody implements CategoryInfoBody {

    private final String message;
    private final Layer layer;
    private List<SubcategoryInfo> subcategoryInfo;

    public CategoryInfoComposedBody(final String message, final Layer layer) {
        this.message = message;
        this.layer = layer;
        subcategoryInfo= new ArrayList<>();
    }

    public void addExceptionBodyLeaf(SubcategoryInfo leaf) {
        subcategoryInfo.add(leaf);
    }
}
