
package com.root.mssm.List.List.citystatelist;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CityStateList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("state")
    @Expose
    private List<State> state = null;
    @SerializedName("city")
    @Expose
    private List<City> city = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

}
