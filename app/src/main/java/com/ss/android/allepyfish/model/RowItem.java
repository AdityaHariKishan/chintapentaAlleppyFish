package com.ss.android.allepyfish.model;

/**
 * Created by dell on 4/30/2017.
 */

public class RowItem {

    private int imageId;
    private String title;
    private String desc;

    public RowItem(Integer image, String title) {
        this.imageId = image;
        this.title = title;
    }

    public String getFishUploadedFromTV() {
        return fishUploadedFromTV;
    }

    public void setFishUploadedFromTV(String fishUploadedFromTV) {
        this.fishUploadedFromTV = fishUploadedFromTV;
    }

    private String fishUploadedFromTV;


    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public RowItem(int imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
    }

    public RowItem() {

    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}
