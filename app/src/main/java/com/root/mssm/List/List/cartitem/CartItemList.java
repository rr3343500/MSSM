
package com.root.mssm.List.List.cartitem;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.root.mssm.List.List.myaddresslist.AddressData;

public class CartItemList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cartdata")
    @Expose
    private List<Cartdatum> cartdata = null;
    @SerializedName("address")
    @Expose
    private List<AddressData> address = null;


    public List<AddressData> getAddress() {
        return address;
    }

    public void setAddress(List<AddressData> address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Cartdatum> getCartdata() {
        return cartdata;
    }

    public void setCartdata(List<Cartdatum> cartdata) {
        this.cartdata = cartdata;
    }

}
