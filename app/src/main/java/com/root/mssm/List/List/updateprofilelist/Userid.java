
package com.root.mssm.List.List.updateprofilelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Userid {

    @SerializedName("photo")
    @Expose
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
