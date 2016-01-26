package com.example.bethechange.movieproject.Favorites;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.bethechange.movieproject.Model.MovieClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Be The Change on 1/26/2016.
 */
public class FavoritesHandler {
    private static final String PrefKey="Favorites";
    public static ArrayList<MovieClass> loadFavorites(Context context) {

        ArrayList<MovieClass> favoritesList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PrefKey, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<MovieClass>>() {
            }.getType();

            favoritesList.clear();
            favoritesList = gson.fromJson(json, type);

        }
        if (favoritesList == null)
            favoritesList = new ArrayList<MovieClass>();
        return favoritesList;
    }

    public static void saveFavorites(ArrayList<MovieClass> favoritesList, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(favoritesList);
        editor.putString(PrefKey, jsonStr);
        editor.commit();
    }
}