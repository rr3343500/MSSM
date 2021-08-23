
package com.root.mssm.List.List.home;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Category")
    @Expose
    private List<Category> category = null;
    @SerializedName("Subcategory")
    @Expose
    private List<Subcategory> subcategory = null;
    @SerializedName("Childcategory")
    @Expose
    private List<Childcategory> childcategory = null;
    @SerializedName("Supercatimage")
    @Expose
    private List<Supercatimage> supercatimage = null;
    @SerializedName("Sliders")
    @Expose
    private List<Slider> sliders = null;
    @SerializedName("Featuredprod")
    @Expose
    private List<Featuredprod> featuredprod = null;
    @SerializedName("Bestprod")
    @Expose
    private List<Bestprod> bestprod = null;
    @SerializedName("Topprod")
    @Expose
    private List<Topprod> topprod = null;
    @SerializedName("Bottomslider")
    @Expose
    private List<Bottomslider> bottomslider = null;
    @SerializedName("Hotprod")
    @Expose
    private List<Hotprod> hotprod = null;
    @SerializedName("Latestprod")
    @Expose
    private List<Latestprod> latestprod = null;
    @SerializedName("Brand")
    @Expose
    private List<Brand> brand = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public List<Subcategory> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<Subcategory> subcategory) {
        this.subcategory = subcategory;
    }

    public List<Childcategory> getChildcategory() {
        return childcategory;
    }

    public void setChildcategory(List<Childcategory> childcategory) {
        this.childcategory = childcategory;
    }

    public List<Supercatimage> getSupercatimage() {
        return supercatimage;
    }

    public void setSupercatimage(List<Supercatimage> supercatimage) {
        this.supercatimage = supercatimage;
    }

    public List<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(List<Slider> sliders) {
        this.sliders = sliders;
    }

    public List<Featuredprod> getFeaturedprod() {
        return featuredprod;
    }

    public void setFeaturedprod(List<Featuredprod> featuredprod) {
        this.featuredprod = featuredprod;
    }

    public List<Bestprod> getBestprod() {
        return bestprod;
    }

    public void setBestprod(List<Bestprod> bestprod) {
        this.bestprod = bestprod;
    }

    public List<Topprod> getTopprod() {
        return topprod;
    }

    public void setTopprod(List<Topprod> topprod) {
        this.topprod = topprod;
    }

    public List<Bottomslider> getBottomslider() {
        return bottomslider;
    }

    public void setBottomslider(List<Bottomslider> bottomslider) {
        this.bottomslider = bottomslider;
    }

    public List<Hotprod> getHotprod() {
        return hotprod;
    }

    public void setHotprod(List<Hotprod> hotprod) {
        this.hotprod = hotprod;
    }

    public List<Latestprod> getLatestprod() {
        return latestprod;
    }

    public void setLatestprod(List<Latestprod> latestprod) {
        this.latestprod = latestprod;
    }

    public List<Brand> getBrand() {
        return brand;
    }

    public void setBrand(List<Brand> brand) {
        this.brand = brand;
    }

}
