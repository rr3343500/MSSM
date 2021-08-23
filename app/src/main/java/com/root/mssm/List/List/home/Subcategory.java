
package com.root.mssm.List.List.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subcategory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("sub_name")
    @Expose
    private String subName;
    @SerializedName("sub_slug")
    @Expose
    private String subSlug;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("dada_categorie")
    @Expose
    private String dadaCategorie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubSlug() {
        return subSlug;
    }

    public void setSubSlug(String subSlug) {
        this.subSlug = subSlug;
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

    public String getDadaCategorie() {
        return dadaCategorie;
    }

    public void setDadaCategorie(String dadaCategorie) {
        this.dadaCategorie = dadaCategorie;
    }

}
