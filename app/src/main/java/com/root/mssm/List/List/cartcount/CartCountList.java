
package com.root.mssm.List.List.cartcount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartCountList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cartcount")
    @Expose
    private Integer cartcount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCartcount() {
        return cartcount;
    }

    public void setCartcount(Integer cartcount) {
        this.cartcount = cartcount;
    }

}
