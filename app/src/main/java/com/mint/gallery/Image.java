package com.mint.gallery;

/**
 * Created by lushijie on 2018/4/20.
 */

public class Image {
    private int id;
    private String path;
    private String title;

    public Image(int id, String path, String title) {
        this.id = id;
        this.path = path;
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Image){
            Image other = (Image) obj;
            return id == other.id;
        }
        return super.equals(obj);
    }
}
