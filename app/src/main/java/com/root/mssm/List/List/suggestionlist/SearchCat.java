
package com.root.mssm.List.List.suggestionlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SearchCat {

    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("catid")
    @Expose
    private String catid;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

}
