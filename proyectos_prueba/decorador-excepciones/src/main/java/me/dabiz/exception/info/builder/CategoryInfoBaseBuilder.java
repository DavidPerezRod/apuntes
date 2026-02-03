package me.dabiz.exception.info.builder;

import me.dabiz.exception.info.category.Layer;

public interface CategoryInfoBaseBuilder {
    MicroBuilder baseInfo(String message, Layer layer);
}


