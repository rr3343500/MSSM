
package com.root.mssm.List.List.searchresult;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResultList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
