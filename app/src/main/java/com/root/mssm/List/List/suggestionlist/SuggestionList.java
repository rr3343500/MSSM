
package com.root.mssm.List.List.suggestionlist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestionList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("search_result")
    @Expose
    private List<SearchResult> searchResult = null;
    @SerializedName("search_cat")
    @Expose
    private List<SearchCat> searchCat = null;
    @SerializedName("search_sub")
    @Expose
    private List<SearchSub> searchSub = null;
    @SerializedName("search_child")
    @Expose
    private List<SearchChild> searchChild = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SearchResult> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<SearchResult> searchResult) {
        this.searchResult = searchResult;
    }

    public List<SearchCat> getSearchCat() {
        return searchCat;
    }

    public void setSearchCat(List<SearchCat> searchCat) {
        this.searchCat = searchCat;
    }

    public List<SearchSub> getSearchSub() {
        return searchSub;
    }

    public void setSearchSub(List<SearchSub> searchSub) {
        this.searchSub = searchSub;
    }

    public List<SearchChild> getSearchChild() {
        return searchChild;
    }

    public void setSearchChild(List<SearchChild> searchChild) {
        this.searchChild = searchChild;
    }

}
