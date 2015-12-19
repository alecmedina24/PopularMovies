package com.xphonesoftware.popularmovies.ui;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alecmedina on 12/11/15.
 */
public class Favorites {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String FAVORITES_KEY = "Favorites";

    private SharedPreferences settings;

    public Favorites(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public void add(String id) {
        Set<String> favorites = settings.getStringSet(FAVORITES_KEY, new HashSet<String>());
        favorites.add(id);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putStringSet(FAVORITES_KEY, favorites);
        editor.commit();
    }

    public void remove(String id) {
        Set<String> favorites = settings.getStringSet(FAVORITES_KEY, new HashSet<String>());
        favorites.remove(id);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putStringSet(FAVORITES_KEY, favorites);
        editor.commit();
    }

    public int size() {
        Set<String> favorites = settings.getStringSet(FAVORITES_KEY, new HashSet<String>());
        return favorites.size();
    }

    public boolean contains(String id) {
        Set<String> favorites = settings.getStringSet(FAVORITES_KEY, new HashSet<String>());
        return favorites.contains(id);
    }

    public Set<String> get(String key) {
        Set<String> favorites = settings.getStringSet(FAVORITES_KEY, new HashSet<String>());
        return favorites;
    }
}
