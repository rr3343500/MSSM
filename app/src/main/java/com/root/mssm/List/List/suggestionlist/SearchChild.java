
package com.root.mssm.List.List.suggestionlist;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SearchChild {

    @SerializedName("child_name")
    @Expose
    private String childName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("childid")
    @Expose
    private String childid;

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getChildid() {
        return childid;
    }

    public void setChildid(String childid) {
        this.childid = childid;
    }

}
