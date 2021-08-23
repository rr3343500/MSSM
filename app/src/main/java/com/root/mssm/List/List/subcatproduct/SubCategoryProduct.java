
package com.root.mssm.List.List.subcatproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryProduct {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Maincategoryprod")
    @Expose
    private List<Subcategoryprod> subcategoryprod = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Subcategoryprod> getSubcategoryprod() {
        return subcategoryprod;
    }

    public void setSubcategoryprod(List<Subcategoryprod> subcategoryprod) {
        this.subcategoryprod = subcategoryprod;
    }

}
