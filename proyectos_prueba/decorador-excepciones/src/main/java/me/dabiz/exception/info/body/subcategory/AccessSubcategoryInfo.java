package me.dabiz.exception.info.body.subcategory;

public class AccessSubcategoryInfo implements SubcategoryInfo {
    private final String user;
    private final String resource;
    private final SubcategoryInfo subcategory;

    public AccessSubcategoryInfo(String user, String resource, SubcategoryInfo subcategory){
        this.user=user;
        this.resource=resource;
        this.subcategory=subcategory;
    }

    public String getType() {
        return subcategory.getType();
    }

    public String getUser(){
        return user;
    }

    public String getResource(){
        return resource;
    }
}
