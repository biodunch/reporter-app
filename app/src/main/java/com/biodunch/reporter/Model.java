package com.biodunch.reporter;

/**
 * Created by Amada on 17/10/2016.
 */

public class Model {
    String name;
    String image;
//    String location;
//    String category;
    String description;

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
