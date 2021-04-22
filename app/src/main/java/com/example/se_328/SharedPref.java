package com.example.se_328;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref
{


    public  static  void  setCity(Context context,String city)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences ("com.example.se_328", Context.MODE_PRIVATE);
        sharedPreferences.edit ().putString ("City",city).apply ();
    }
    public  static  String  getCity(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences ("com.example.se_328", Context.MODE_PRIVATE);
        return  sharedPreferences.getString ("City","Athens");
    }

}
