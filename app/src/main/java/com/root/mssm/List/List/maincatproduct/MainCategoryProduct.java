
package com.root.mssm.List.List.maincatproduct;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainCategoryProduct {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Maincategoryprod")
    @Expose
    private List<Maincategoryprod> maincategoryprod = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Maincategoryprod> getMaincategoryprod() {
        return maincategoryprod;
    }

    public void setMaincategoryprod(List<Maincategoryprod> maincategoryprod) {
        this.maincategoryprod = maincategoryprod;
    }

}
