package me.dabiz.exception.info.category.access;

import me.dabiz.exception.info.category.Category;

public class AccessCategoryInfo implements Category {
    private final String user;
    private final String resource;
    private final Category category;

    public AccessCategoryInfo(String user, String resource, Category category){
        this.user=user;
        this.resource=resource;
        this.category =category;
    }

    public Object getCategory() {
        return category.getCategory();
    }

    public String getUser(){
        return user;
    }

    public String getResource(){
        return resource;
    }
}
