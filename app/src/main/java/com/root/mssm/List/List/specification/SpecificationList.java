
package com.root.mssm.List.List.specification;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecificationList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Specification")
    @Expose
    private List<Specification> specification = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Specification> getSpecification() {
        return specification;
    }

    public void setSpecification(List<Specification> specification) {
        this.specification = specification;
    }

}
