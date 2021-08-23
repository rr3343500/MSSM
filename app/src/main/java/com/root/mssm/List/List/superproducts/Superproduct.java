
package com.root.mssm.List.List.superproducts;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Superproduct {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Superprod")
    @Expose
    private List<Superprod> superprod = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Superprod> getSuperprod() {
        return superprod;
    }

    public void setSuperprod(List<Superprod> superprod) {
        this.superprod = superprod;
    }

}
