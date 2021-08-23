package com.root.mssm.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class SessionManage {

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences cart;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "UserSessionPref";

    // First time logic Check
    public static final String FIRST_TIME = "firsttime";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static  final  String NAME ="name";
    public static  final  String EMAIL ="email";
    public static  final  String MOBILE ="mobile";
    public static  final  String ADDRESS ="address";
    public static  final  String ADDRESSID ="addressid";
    public static  final  String REGISTRATION ="registration";
    public static  final  String STOREPIC ="storepic";
    public static  final  String USERID ="userid";
    public static  final  String USERTYPE ="usertype";
    public static  final  String PASSWORD ="password";

    // Cart
    public static  final String CART = "cart";



    private static SessionManage ourInstance = null;


    public static SessionManage getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SessionManage(context);
        }
        return ourInstance;
    }

    // Constructor
    public SessionManage(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void Logout(){
        editor.clear().commit();
    }

    public String getUserid(){
        return pref.getString(USERID, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(NAME,pref.getString(NAME,""));
        user.put(EMAIL,pref.getString(EMAIL,""));
        user.put(MOBILE,pref.getString(MOBILE,""));
        user.put(ADDRESS,pref.getString(ADDRESS,""));
        user.put(ADDRESSID,pref.getString(ADDRESSID,""));
        user.put(REGISTRATION,pref.getString(REGISTRATION,""));
        user.put(STOREPIC,pref.getString(STOREPIC,""));
        user.put(USERID,pref.getString(USERID,Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)));
        user.put(USERTYPE,pref.getString(USERTYPE,""));
        user.put(PASSWORD,pref.getString(PASSWORD,""));

        // return user
        return user;
    }

    public void SetDefaultAddress(String address, String addressID){
        editor.putString(ADDRESS,address);
        editor.putString(ADDRESSID,addressID);
        editor.commit();
    }

    public void ClearAddress(){
        editor.putString(ADDRESS,null);
        editor.putString(ADDRESSID,null);
        editor.commit();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void UserDetail(String id, String name, String email, String mobile, String user_type, String password,String address,String shoppic ,String registration) {
        editor.putString(NAME,name);
        editor.putString(EMAIL,email);
        editor.putString(MOBILE,mobile);
        editor.putString(USERTYPE,user_type);
        editor.putString(USERID,id);
        editor.putString(PASSWORD,password);
        editor.putString(ADDRESS,address);
        editor.putString(STOREPIC,shoppic);
        editor.putString(REGISTRATION,registration);
        editor.commit();
    }

    public void  setIsLogin(){
        editor.putBoolean(IS_LOGIN,true);
        editor.commit();
    }



//    void s(String name,String ,String description,String image_path, String image_name,String category1, String category1_image_path, String category1_image_name, String category2,  String category3, String  shop_id , String shop_name, String onMRP, String veg, String instock,String _id, String actual_price , String discounted_price, String createdAt)




    public void AddtoCart(String name,String description,String image_path, String image_name,String category1, String category1_image_path,
                          String category1_image_name, String category2,  String category3, String  shop_id , String shop_name, String onMRP, String veg,
                          String instock,String _id, String actual_price , String discounted_price, String createdAt, String qty, boolean type){

        if(pref.getString(CART,null)==null || pref.getString(CART,null).equals("") ){

           setCart(name, description,image_path,image_name,category1, category1_image_path,category1_image_name,category2,category3,shop_id,shop_name,onMRP,
                   veg, instock,_id,actual_price,discounted_price ,createdAt, qty , type);
        }else {
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(pref.getString(CART,null));
                if(SearchCart(jsonObject,_id).equals("")){
                    JSONObject temp=  new JSONObject();
                    temp.put("name", name);
                    temp.put("description", description);
                    temp.put("image_path", image_path);
                    temp.put("image_name", image_name);
                    temp.put("category1", category1);
                    temp.put("category1_image_path", category1_image_path);
                    temp.put("category1_image_name", category1_image_name);
                    temp.put("category2", category2);
                    temp.put("category3", category3);
                    temp.put("shop_id", shop_id);
                    temp.put("shop_name", shop_name);
                    temp.put("onMRP", onMRP);
                    temp.put("veg", veg);
                    temp.put("instock", instock);
                    temp.put("id", _id);
                    temp.put("actual_price", actual_price);
                    temp.put("discounted_price", discounted_price);
                    temp.put("createdAt", createdAt);
                    temp.put("qty", qty);
                    jsonObject.put(_id, temp);
//                    jsonObject.put("shopid",shop);
                    editor.putString(CART,jsonObject.toString()).commit();
                }else {
                    updatecart(jsonObject,name, description,image_path,image_name,category1, category1_image_path,category1_image_name,category2,category3,
                            shop_id,shop_name,onMRP, veg, instock, _id, actual_price,discounted_price ,createdAt,qty, type);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            editor.commit();
        }

        Log.e("TAG", "AddtoCart: "+ pref.getString(CART,null).toString());

    }



    String SearchCart(JSONObject jsonObject, String id){
        try {
            return jsonObject.getString(id);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    void setCart(String name,String description,String image_path, String image_name,String category1, String category1_image_path,
                 String category1_image_name, String category2,  String category3, String  shop_id , String shop_name, String onMRP, String veg,
                 String instock,String _id, String actual_price , String discounted_price, String createdAt, String qty , boolean type){

        JSONObject temp=  new JSONObject();
        JSONObject finaljson = new JSONObject();
        try {
            temp.put("name", name);
            temp.put("description", description);
            temp.put("image_path", image_path);
            temp.put("image_name", image_name);
            temp.put("category1", category1);
            temp.put("category1_image_path", category1_image_path);
            temp.put("category1_image_name", category1_image_name);
            temp.put("category2", category2);
            temp.put("category3", category3);
            temp.put("shop_id", shop_id);
            temp.put("shop_name", shop_name);
            temp.put("onMRP", onMRP);
            temp.put("veg", veg);
            temp.put("instock", instock);
            temp.put("id", _id);
            temp.put("actual_price", actual_price);
            temp.put("discounted_price", discounted_price);
            temp.put("createdAt", createdAt);
            temp.put("qty", qty);

//            finaljson.put("shopid",shop);
            finaljson.put(_id, temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(CART,finaljson.toString()).commit();
    }

    void updatecart(JSONObject jsonObject, String name,String description,String image_path, String image_name,String category1, String category1_image_path,
                    String category1_image_name, String category2,  String category3, String  shop_id , String shop_name, String onMRP, String veg,
                    String instock,String _id, String actual_price , String discounted_price, String createdAt, String qty , boolean type){
            JSONObject temp=  new JSONObject();
            JSONObject fina = new JSONObject();
            try {
                JSONObject jsonObject1= new JSONObject(jsonObject.getString(_id));
                int Qty = jsonObject1.getInt("qty");
                if(type){
                    Qty++;
                    temp.put("name", name);
                    temp.put("description", description);
                    temp.put("image_path", image_path);
                    temp.put("image_name", image_name);
                    temp.put("category1", category1);
                    temp.put("category1_image_path", category1_image_path);
                    temp.put("category1_image_name", category1_image_name);
                    temp.put("category2", category2);
                    temp.put("category3", category3);
                    temp.put("shop_id", shop_id);
                    temp.put("shop_name", shop_name);
                    temp.put("onMRP", onMRP);
                    temp.put("veg", veg);
                    temp.put("instock", instock);
                    temp.put("id", _id);
                    temp.put("actual_price", actual_price);
                    temp.put("discounted_price", discounted_price);
                    temp.put("createdAt", createdAt);
                    temp.put("qty", Qty);
                    jsonObject.put(_id, temp);
//                    jsonObject.put("shopid",shop);
                    editor.putString(CART,jsonObject.toString()).commit();
                    }
                else
                    {
                        Qty--;
                        if(Qty == 0){
                            removefromCart(_id);
                        }else {
                            temp.put("name", name);
                            temp.put("description", description);
                            temp.put("image_path", image_path);
                            temp.put("image_name", image_name);
                            temp.put("category1", category1);
                            temp.put("category1_image_path", category1_image_path);
                            temp.put("category1_image_name", category1_image_name);
                            temp.put("category2", category2);
                            temp.put("category3", category3);
                            temp.put("shop_id", shop_id);
                            temp.put("shop_name", shop_name);
                            temp.put("onMRP", onMRP);
                            temp.put("veg", veg);
                            temp.put("instock", instock);
                            temp.put("id", _id);
                            temp.put("actual_price", actual_price);
                            temp.put("discounted_price", discounted_price);
                            temp.put("createdAt", createdAt);
                            temp.put("qty", Qty);
                            jsonObject.put(_id, temp);
//                            jsonObject.put("shopid",shop);
                            editor.putString(CART,jsonObject.toString()).commit();
                        }
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }



    }

    void removefromCart(String id){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(pref.getString(CART, null));
            jsonObject.remove(id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString(CART,jsonObject.toString()).commit();

    }

    public String getitemfromcart(String id){
        String qty="";
        if(pref.getString(CART,null)==null || pref.getString(CART,null).equals("") ){
            qty="";
        }else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(pref.getString(CART, null));
                if(SearchCart(jsonObject,id).equals(""))
                {
                    qty="";
                }else {
                    JSONObject jsonObject1= new JSONObject(jsonObject.getString(id));
                    int Qty = jsonObject1.getInt("qty");
                    qty= String.valueOf(Qty);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
      return qty;
    }

    public String getCertCount(){
        String qty="0";
        if(pref.getString(CART,null)==null || pref.getString(CART,null).equals("") ){
            qty="0";
        }else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(pref.getString(CART, null));
                qty = String.valueOf(jsonObject.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return qty;
    }

    public void clearCart(){
        editor.remove(CART).commit();
    }

    public boolean cartShop(String id, String itemid){
        boolean status =true;
        if(pref.getString(CART,null)==null || pref.getString(CART,null).equals("") ){
            status = true;
        }else {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(pref.getString(CART, null));
                String ids="";
                Iterator<String> iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
//                        JSONObject jsonObject1= new JSONObject(key);
                        JSONObject jsonObject1= jsonObject.getJSONObject(key);
                        if(jsonObject1.getString("shop_id").equals(id)){
                            status = true;
                        }else {
                            status= false;
                        }
                    } catch (JSONException e) {
                        // Something went wrong!
                        Log.e("TAG", "cartShop: "+ e.getMessage() );
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return status;
    }


    public  String getCartItem(){
        return pref.getString(CART, null);
    }


    public double getOrderAmount(){
        double Amt = 0;
        JSONObject jsonObject = null;
        if(getCartItem()!=null) {
            try {
                jsonObject = new JSONObject(getCartItem());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(jsonObject.length()!=0){
                String ids = "";
                Iterator<String> iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(key);
//                        cartLists.add(new CartList(jsonObject1.getString("name"),
//                                jsonObject1.getString("img"),
//                                jsonObject1.getString("price"),
//                                "",
//                                jsonObject1.getString("id"),
//                                jsonObject1.getString("shop"),
//                                jsonObject1.getString("qty")));
                        double qty= Double.parseDouble(jsonObject1.getString("qty"));
                        double price= Double.parseDouble(jsonObject1.getString("discounted_price"));
                        Amt = Amt + (qty * price);

                    } catch (JSONException e) {
                        Log.e("TAG", "cartShop: " + e.getMessage());
                    }
                }
            }
            else {

            }

        }else {

        }



        return Amt;
    }
}




























