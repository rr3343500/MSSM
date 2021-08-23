
package com.root.mssm.List.List.cartitem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Productdatum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("cprice")
    @Expose
    private String cprice;
    @SerializedName("pprice")
    @Expose
    private String pprice;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sshipingamount")
    @Expose
    private String sshipingamount;

    public String getSshipingamount() {
        return sshipingamount;
    }

    public void setSshipingamount(String sshipingamount) {
        this.sshipingamount = sshipingamount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCprice() {
        return cprice;
    }

    public void setCprice(String cprice) {
        this.cprice = cprice;
    }

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
