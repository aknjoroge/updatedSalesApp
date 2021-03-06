package com.example.ecommerce;

import android.content.Context;
import android.content.SharedPreferences;

public class lanchermanager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static String PREF_NAME = "lanchermanager";
    private static String IS_FIRST_TIME = "isFirst";

    public lanchermanager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME,0);
        editor = sharedPreferences.edit();
    }

    public void setFirstLunch(boolean isFirst){
        editor.putBoolean(IS_FIRST_TIME,isFirst);
        editor.commit();
    }

    public boolean isFirstTime(){
        return sharedPreferences.getBoolean(IS_FIRST_TIME,true);
    }
}

