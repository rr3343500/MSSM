
package com.root.mssm.List.List.productdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Productgallery {

    @SerializedName("photo")
    @Expose
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Productgallery(String photo) {
        this.photo = photo;
    }
}
