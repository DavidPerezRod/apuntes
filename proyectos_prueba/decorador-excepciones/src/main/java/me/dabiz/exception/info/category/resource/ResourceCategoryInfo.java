package me.dabiz.exception.info.category.resource;

import me.dabiz.exception.info.category.Category;

public class ResourceCategoryInfo implements Category {
    private final Resource resource;
    private final Category category;

    ResourceCategoryInfo(final Resource resource, final Category subcategory){
        this.resource=resource;
        this.category =subcategory;
    }


    @Override
    public Object getCategory() {
        return category.getCategory();
    }

    public Resource getResource(){
        return resource;
    }
}
