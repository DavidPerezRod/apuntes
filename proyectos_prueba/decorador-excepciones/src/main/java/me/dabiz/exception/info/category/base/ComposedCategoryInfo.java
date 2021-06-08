package me.dabiz.exception.info.category.base;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import me.dabiz.exception.info.category.Layer;
import me.dabiz.exception.info.category.Category;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonPropertyOrder({"categoryInfo", "layer", "message"})
public class ComposedCategoryInfo implements ICategoryInfo {

    private final String message;
    private final Layer layer;
    private List<Category> categoriesInfo;

    public ComposedCategoryInfo(final String message, final Layer layer) {
        this.message = message;
        this.layer = layer;
        categoriesInfo = new ArrayList<>();
    }

    public void addCategoryInfo(Category leaf) {
        categoriesInfo.add(leaf);
    }
}
