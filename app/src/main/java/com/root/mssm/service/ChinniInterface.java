package com.root.mssm.service;


import com.google.gson.JsonObject;
import com.root.mssm.List.List.Contactus.ContactUs;
import com.root.mssm.List.List.addnewaddress.AddNewAddress;
import com.root.mssm.List.List.cart.CartList;
import com.root.mssm.List.List.cartcount.CartCountList;
import com.root.mssm.List.List.cartitem.CartItemList;
import com.root.mssm.List.List.changepassword.ChangePasswordList;
import com.root.mssm.List.List.citystatelist.CityStateList;
import com.root.mssm.List.List.maincatproduct.MainCategoryProduct;
import com.root.mssm.List.List.myaddresslist.MyAddressList;
import com.root.mssm.List.List.privacylist.PrivacyList;
import com.root.mssm.List.List.productdetail.ProductDetailList;
import com.root.mssm.List.List.removeaddress.RemoveAddressList;
import com.root.mssm.List.List.searchresult.SearchResultList;
import com.root.mssm.List.List.signup.Signup;
import com.root.mssm.List.List.home.HomeList;
import com.root.mssm.List.List.specification.SpecificationList;
import com.root.mssm.List.List.subcatproduct.SubCategoryProduct;
import com.root.mssm.List.List.suggestionlist.SuggestionList;
import com.root.mssm.List.List.superproducts.Superproduct;
import com.root.mssm.List.List.updateprofilelist.UpdateProfileList;
import com.root.mssm.List.List.walletlist.WalletHistoryList;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ChinniInterface {

    @POST("userregister")
    Call<Signup>SIGNUP_CALL(@Body JsonObject jsonObject);

    @POST("login")
    Call<Signup>LOGINCall(@Body JsonObject jsonObject);

    @POST("getdynamiccart")
    Call<CartList>CART_LIST_CALL(@Body JsonObject jsonObject);

    @POST("contactusapi")
    Call<ContactUs>CONTACT_US_API(@Body JsonObject jsonObject);

    @POST("adduseraddress")
    Call<AddNewAddress>ADD_NEW_ADDRESS(@Body JsonObject jsonObject);

    @POST("updateadd")
    Call<AddNewAddress>UPDATE_ADDRESS(@Body JsonObject jsonObject);

    @GET("homepageapi")
    Call<HomeList>HOME_LIST_CALL();

    @GET("statecity")
    Call<CityStateList>CITY_STATE_LIST_CALL();

    @GET("superprod")
    Call<Superproduct>SUPERPRODUCT_CALL(@Query("id") String id);

    @GET("wallethistory")
    Call<WalletHistoryList>WALLET_HISTORY_LIST_CALL(@Query("userid") String id);

    @GET("maincateprod")
    Call<MainCategoryProduct>MAINCATPRODUCT_CALL(@Query("id") String id);

    @GET("maincateprod")
    Call<SubCategoryProduct>SUB_CATEGORY_PRODUCT_CALL(@Query("id") String id);

    @GET("productdetails")
    Call<ProductDetailList>PRODUCTDETAIL_CALL(@Query("id") String id,@Query("user_id") String user_id );

    @GET("prodspec")
    Call<SpecificationList>SPECIFICATION_LIST_CALL(@Query("id") String id);

    @GET("cartcount")
    Call<CartCountList>COUNT_LIST_CALL(@Query("user_id") String id);

    @GET("getuseraddress")
    Call<MyAddressList>MY_ADDRESS_LIST_CALL(@Query("userid") String id);

    @GET("getallpages")
    Call<PrivacyList>PRIVACY_LIST_CALL();

    @GET("profile")
    Call<Signup>PROFILE_LIST_CALL(@Query("userid") String id);

    @POST("userupdateprofile")
    @Multipart
    Call<UpdateProfileList> PROFILE_UPDATE_CALL(@Part MultipartBody.Part file, @Part("name") RequestBody s, @Part("userid") RequestBody s1);

    @GET("cartdata")
    Call<CartItemList>ITEM_LIST_CALL(@Query("user_id") String id);

    @POST("removedynamiccart")
    @FormUrlEncoded
    Call<JSONObject>REMOVE_FROM_CART(@Field("user_id") String id , @Field("cart_id") String product_id);

    @POST("deleteaddress")
    @FormUrlEncoded
    Call<RemoveAddressList>REMOVE_ADDRESS_LIST_CALL(@Field("id") String s);

    @POST("changpass")
    Call<ChangePasswordList>CHANGE_PASSWORD_LIST_CALL(@Body JsonObject jsonObject);

    @POST("searchreldata")
    @FormUrlEncoded
    Call<SearchResultList>SEARCHRESULT(@Field("catid") String catid,@Field("prodid") String prodid,@Field("subid") String subid,@Field("childid") String childid);

    @POST("searchsuggest")
    @FormUrlEncoded
    Call<SuggestionList>SUGGESTION_LIST_CALL(@Field("search") String id);

    @POST("updatecartqty")
    @FormUrlEncoded
    Call<JSONObject>UPDATE_CART_QUANTITY(@Field("id") String id , @Field("quantity") String quantity, @Field("product_id") String product_id);





}
