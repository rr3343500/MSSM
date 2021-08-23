
package com.root.mssm.List.List.suggestionlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SearchSub {

    @SerializedName("sub_name")
    @Expose
    private String subName;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("subid")
    @Expose
    private String subid;

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

}
