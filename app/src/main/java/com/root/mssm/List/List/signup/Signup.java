
package com.root.mssm.List.List.signup;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Signup {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userid")
    @Expose
    private Userid userid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Userid getUserid() {
        return userid;
    }

    public void setUserid(Userid userid) {
        this.userid = userid;
    }

}
