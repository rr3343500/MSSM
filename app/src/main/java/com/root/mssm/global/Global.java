package com.root.mssm.global;

import android.app.Application;

import com.root.mssm.List.List.home.HomeList;
import com.root.mssm.List.List.myaddresslist.AddressData;
import com.root.mssm.List.List.productdetail.ProductDetailList;
import com.root.mssm.List.List.specification.SpecificationList;

import java.util.ArrayList;
import java.util.List;

public class Global extends Application {
    List<HomeList>homeLists= new ArrayList<>();

    ProductDetailList productdetail= new ProductDetailList();

    SpecificationList specificationList= new SpecificationList();

    AddressData addressData = new AddressData();


    public List<HomeList> getHomeLists() {
        return homeLists;
    }

    public void setHomeLists(List<HomeList> homeLists) {
        this.homeLists = homeLists;
    }

    public ProductDetailList getProductdetail() {
        return productdetail;
    }

    public void setProductdetail(ProductDetailList productdetail) {
        this.productdetail = productdetail;
    }

    public SpecificationList getSpecificationList() {
        return specificationList;
    }

    public void setSpecificationList(SpecificationList specificationList) {
        this.specificationList = specificationList;
    }

    public AddressData getAddressData() {
        return addressData;
    }

    public void setAddressData(AddressData addressData) {
        this.addressData = addressData;
    }
}
