package com.example.tornstocks.Repositories;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiKeyRepository {
    private static final String SHARED_PREFS = "com.example.tornstocks.Repositories.SHARED_PREFS";
    private static final String API_KEY_KEY = "com.example.tornstocks.Repositories.API_KEY_KEY";

    private static String apiKey = null;


    public static void setApiKey(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(API_KEY_KEY, key).commit();
        apiKey = key;
    }

    public static String getApiKey(Context context) {
        if (apiKey == null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            apiKey = sharedPreferences.getString(API_KEY_KEY, "");
        }
        return apiKey;
    }
}
