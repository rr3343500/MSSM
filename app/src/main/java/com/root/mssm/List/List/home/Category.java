
package com.root.mssm.List.List.home;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Category {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("cat_slug")
    @Expose
    private String catSlug;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("slider_photo")
    @Expose
    private String sliderPhoto;
    @SerializedName("dada_categorie")
    @Expose
    private String dadaCategorie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatSlug() {
        return catSlug;
    }

    public void setCatSlug(String catSlug) {
        this.catSlug = catSlug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSliderPhoto() {
        return sliderPhoto;
    }

    public void setSliderPhoto(String sliderPhoto) {
        this.sliderPhoto = sliderPhoto;
    }

    public String getDadaCategorie() {
        return dadaCategorie;
    }

    public void setDadaCategorie(String dadaCategorie) {
        this.dadaCategorie = dadaCategorie;
    }

}
