
package com.root.mssm.List.List.cart;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.root.mssm.List.List.myaddresslist.AddressData;


public class CartList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("address")
    @Expose
    private List<AddressData> address = null;


    public List<AddressData> getAddress() {
        return address;
    }

    public void setAddress(List<AddressData> address) {
        this.address = address;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
