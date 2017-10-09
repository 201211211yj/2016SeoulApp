package com.jeman.myapp.model;

public class ItemSlideMenu {

    private int imgId;
    private String title;

    public ItemSlideMenu(int imgId, String title) {
        this.title = title;
        this.imgId = imgId;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getImgId() { return imgId; }

    public void setImgId(int imgId) { this.imgId = imgId; }
}
