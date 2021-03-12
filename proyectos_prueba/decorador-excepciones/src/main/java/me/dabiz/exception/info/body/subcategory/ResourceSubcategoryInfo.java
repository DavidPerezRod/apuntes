package me.dabiz.exception.info.body.subcategory;

import me.dabiz.exception.info.Resource;

public class ResourceSubcategoryInfo implements SubcategoryInfo {
    private final Resource resource;
    private final SubcategoryInfo subcategory;

    ResourceSubcategoryInfo(final Resource resource, final SubcategoryInfo subcategory){
        this.resource=resource;
        this.subcategory=subcategory;
    }


    @Override
    public String getType() {
        return subcategory.getType();
    }

    public Resource getResource(){
        return resource;
    }
}
