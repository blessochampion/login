package com.loginapp.login.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Blessing.Ekundayo on 9/5/2017.
 */

public class UserPreference
{
    private static final String USER_PREFERENCE_NAME = "user_preference";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_POINT = "point";
    private static final String KEY_USER_LOGGED_IN = "logged_in";
SharedPreferences sharedPreferences;
    static UserPreference INSTANCE;

    private UserPreference(Context context){
        sharedPreferences = context.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setUsername(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, name);
        editor.commit();
    }
    public boolean isUserLoggedIn(){
       return sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false);
    }

    public void setUserLoggedIn(boolean isLoggedIn){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_USER_LOGGED_IN, isLoggedIn);
        editor.commit();
    }
    public String getUsername(){
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void setUserId(long userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_USER_ID, userId);
        editor.commit();
    }

    public long getUserId(){
        return sharedPreferences.getLong(KEY_USER_ID, 0);
    }

    public void setUserPoint(int point){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_POINT, point);
        editor.commit();
    }

    public int getPoint(){
        return  sharedPreferences.getInt(KEY_POINT, -1);
    }

    public  static  UserPreference getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new UserPreference(context);
        }

        return INSTANCE;
    }
}