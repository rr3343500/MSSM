
package com.root.mssm.List.List.productdetail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Productdetail")
    @Expose
    private List<Productdetail> productdetail = null;
    @SerializedName("Productgallery")
    @Expose
    private List<Productgallery> productgallery = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Productdetail> getProductdetail() {
        return productdetail;
    }

    public void setProductdetail(List<Productdetail> productdetail) {
        this.productdetail = productdetail;
    }

    public List<Productgallery> getProductgallery() {
        return productgallery;
    }

    public void setProductgallery(List<Productgallery> productgallery) {
        this.productgallery = productgallery;
    }

}
