package com.root.mssm.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.root.mssm.List.List.home.HomeList;
import com.root.mssm.List.List.signup.Userid;

import java.util.Collections;
import java.util.List;

public class Cookies {


    public static final String CACHEDATA = "cachedata";
    public static final String APPDATA = "appdata";
    public static final String PREFS_NAME = "Detail";
    public static final String FAVORITES = "userdata";

    public Cookies() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, Userid favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Userid product) {
            saveFavorites(context, product);
    }

    public void removeFavorite(Context context) {

    }

    public Userid getFavorites(Context context) {
        SharedPreferences settings;
        Userid favorites = new Userid();

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Userid favoriteItems = gson.fromJson(jsonFavorites, Userid.class);

            favorites = favoriteItems;
        } else
            return favorites;

        return favorites;
    }


    public void CacheDate(Context context, HomeList favorites){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(CACHEDATA,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(APPDATA, jsonFavorites);

        editor.commit();
    }

    public List<HomeList> getAppdata(Context context) {
        SharedPreferences settings;
        HomeList favorites = new HomeList();

        settings = context.getSharedPreferences(CACHEDATA,
                Context.MODE_PRIVATE);

        if (settings.contains(APPDATA)) {
            String jsonFavorites = settings.getString(APPDATA, null);
            Gson gson = new Gson();
            HomeList favoriteItems = gson.fromJson(jsonFavorites, HomeList.class);

            favorites = favoriteItems;
        } else
            return Collections.singletonList(favorites);

        return Collections.singletonList(favorites);
    }

}
