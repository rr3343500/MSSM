
package com.root.mssm.List.List.profilelist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.root.mssm.List.List.signup.Userid;


public class ProfileList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userid")
    @Expose
    private List<Userid> userid = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Userid getData() {
        return (Userid) userid;
    }

    public void setData(List<Userid> data) {
        this.userid = data;
    }

}
