
package com.root.mssm.List.List.privacylist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrivacyList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("getallpages")
    @Expose
    private List<Getallpage> getallpages = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Getallpage> getGetallpages() {
        return getallpages;
    }

    public void setGetallpages(List<Getallpage> getallpages) {
        this.getallpages = getallpages;
    }

}
