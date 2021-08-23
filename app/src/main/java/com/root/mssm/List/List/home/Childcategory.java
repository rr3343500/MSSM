
package com.root.mssm.List.List.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Childcategory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("subcategory_id")
    @Expose
    private String subcategoryId;
    @SerializedName("child_name")
    @Expose
    private String childName;
    @SerializedName("child_slug")
    @Expose
    private String childSlug;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("photo")
    @Expose
    private String photo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildSlug() {
        return childSlug;
    }

    public void setChildSlug(String childSlug) {
        this.childSlug = childSlug;
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

}
