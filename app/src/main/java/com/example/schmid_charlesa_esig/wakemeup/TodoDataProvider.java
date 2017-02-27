package com.example.schmid_charlesa_esig.wakemeup;

/**
 * Created by Charles on 17.02.2017.
 */

public class TodoDataProvider {
    private int poster_resource;
    private String item_title;
    private String item_desc;

    public TodoDataProvider(int poster_resource, String item_title, String item_desc){
        this.setPoster_resource(poster_resource);
        this.setItem_title(item_title);
        this.setItem_desc(item_desc);
    }

    public int getPoster_resource() {
        return poster_resource;
    }


    public void setPoster_resource(int poster_resource) {
        this.poster_resource = poster_resource;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }
}
