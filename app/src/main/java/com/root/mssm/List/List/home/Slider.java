
package com.root.mssm.List.List.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slider {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("title_size")
    @Expose
    private String titleSize;
    @SerializedName("title_color")
    @Expose
    private String titleColor;
    @SerializedName("title_anime")
    @Expose
    private String titleAnime;
    @SerializedName("desc_size")
    @Expose
    private String descSize;
    @SerializedName("desc_color")
    @Expose
    private String descColor;
    @SerializedName("desc_anime")
    @Expose
    private String descAnime;
    @SerializedName("link")
    @Expose
    private String link;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(String titleSize) {
        this.titleSize = titleSize;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getTitleAnime() {
        return titleAnime;
    }

    public void setTitleAnime(String titleAnime) {
        this.titleAnime = titleAnime;
    }

    public String getDescSize() {
        return descSize;
    }

    public void setDescSize(String descSize) {
        this.descSize = descSize;
    }

    public String getDescColor() {
        return descColor;
    }

    public void setDescColor(String descColor) {
        this.descColor = descColor;
    }

    public String getDescAnime() {
        return descAnime;
    }

    public void setDescAnime(String descAnime) {
        this.descAnime = descAnime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
